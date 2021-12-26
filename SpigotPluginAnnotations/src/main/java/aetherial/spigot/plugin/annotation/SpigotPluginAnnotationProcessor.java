package aetherial.spigot.plugin.annotation;

import aetherial.spigot.plugin.annotation.command.CommandTag;
import aetherial.spigot.plugin.annotation.command.CommandsTag;
import aetherial.spigot.plugin.annotation.permission.ChildPermission;
import aetherial.spigot.plugin.annotation.permission.ChildPermissions;
import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import aetherial.spigot.plugin.annotation.permission.PermissionsTag;
import aetherial.spigot.plugin.annotation.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SupportedAnnotationTypes("aetherial.spigot.plugin.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class SpigotPluginAnnotationProcessor extends AbstractProcessor {

    private static final String OUTPUT_FILE = "plugin.yml";

    private static final String MAIN = "main";
    private static final String NAME = "name";
    private static final String VERSION = "version";
    private static final String DESCRIPTION = "description";
    private static final String API_VERSION = "api-version";
    private static final String LOAD = "load";
    private static final String AUTHOR = "author";
    private static final String AUTHORS = "authors";
    private static final String WEBSITE = "website";
    private static final String DEPEND = "depend";
    private static final String PREFIX = "prefix";
    private static final String SOFT_DEPEND = "softdepend";
    private static final String LOAD_BEFORE = "loadbefore";
    private static final String COMMANDS = "commands";
    private static final String PERMISSIONS = "permissions";
    private static final String ALIASES = "aliases";
    private static final String PERMISSION = "permission";
    private static final String PERMISSION_MESSAGE = "permission-message";
    private static final String USAGE = "usage";
    private static final String DEFAULT = "default";
    private static final String CHILDREN = "children";

    private static final Map<String, Object> data = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Object>> commandsBlock = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Object>> permissionsBlock = new ConcurrentHashMap<>();
    private static final List<ChildPermission> childPermissions = Collections.synchronizedList(new ArrayList<>());

    private boolean foundMainClass = false;
    private Element pluginClass = null;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean lastRound = roundEnv.processingOver(); // Should write file on last round

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedClasses = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element annotatedClass : annotatedClasses) {
                processAnnotation(annotatedClass, annotation);
            }
        }

        if (!lastRound) {
            // Do not claim any annotations. This allows them to be available on the last round
            return true;
        }

        finalizeData();
        return writePluginYaml();
    }

    /**
     * Final steps before writing the plugin.yml file
     */
    private void finalizeData() {
        // Process the Plugin annotated Class
        processPluginAnnotation(pluginClass);

        // Add commands, if they exist
        if (!commandsBlock.isEmpty()) {
            data.put(COMMANDS, commandsBlock);
        }

        // Add child permissions, if they exist
        for (ChildPermission childPermission : childPermissions) {
            String parent = childPermission.parent();
            if (!permissionsBlock.containsKey(parent)) {
                raiseError("Invalid parent for child permission: " + parent);
                continue;
            }
            Map<String, Object> parentBlock = permissionsBlock.get(parent);
            //noinspection unchecked, Forcing this value to be a Map
            Map<String, Object> childrenBlock = (Map<String, Object>) parentBlock.computeIfAbsent(CHILDREN, key -> new HashMap<>());
            childrenBlock.put(childPermission.name(), childPermission.inherit());
        }

        // Add permissions, if they exist
        if (!permissionsBlock.isEmpty()) {
            data.put(PERMISSIONS, permissionsBlock);
        }
    }

    /**
     * Write the plugin.yml file
     *
     * @return True if the file was created, otherwise false
     */
    private boolean writePluginYaml() {
        try (Writer writer = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE)
            .openWriter()) {
            String raw = buildYamlFile();
            writer.write(raw);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            raiseError("Failed to create plugin.yml file");
            return false;
        }
        return true;
    }

    /**
     * Builds the Yaml file in the "pretty" order
     *
     * @return The contents for the plugin.yml file
     */
    private String buildYamlFile() {
        PluginYamlBuilder builder = new PluginYamlBuilder(data);
        builder.append(MAIN).append(NAME).append(VERSION).append(DESCRIPTION).append(API_VERSION)
            .append(LOAD).append(AUTHOR).append(AUTHORS).append(WEBSITE).append(DEPEND)
            .append(PREFIX).append(LOAD_BEFORE).append(COMMANDS).append(PERMISSIONS);

        return builder.getRawYamlContents();
    }

    /**
     * Process the annotated Class
     * <p>
     * May save or skip the annotation to be processed at a later time
     *
     * @param annotatedClass The annotated Class
     * @param annotation     The Annotation
     */
    private void processAnnotation(Element annotatedClass, TypeElement annotation) {
        String annotationName = annotation.asType().toString();
        annotationName = annotationName.substring(annotationName.lastIndexOf('.') + 1);

        if (annotationName.equals(Plugin.class.getSimpleName())) {
            if (foundMainClass) {
                raiseError("Already found a main class annotated with @Plugin! Aborting, more than one class with @Plugin");
                return;
            }
            // Will be processed at the end. This makes sure that all other possible annotates are discovered on the same Class
            foundMainClass = true;
            pluginClass = annotatedClass;
        } else if (annotationName.equals(CommandsTag.class.getSimpleName())) {
            processCommandsAnnotation(annotatedClass);
        } else if (annotationName.equals(CommandTag.class.getSimpleName())) {
            processCommandAnnotation(annotatedClass);
        } else if (annotationName.equals(PermissionsTag.class.getSimpleName())) {
            processPermissionsAnnotation(annotatedClass);
        } else if (annotationName.equals(PermissionTag.class.getSimpleName())) {
            processPermissionAnnotation(annotatedClass);
        } else if (annotationName.equals(ChildPermissions.class.getSimpleName())) {
            processChildPermissionsAnnotation(annotatedClass);
        } else if (annotationName.equals(ChildPermission.class.getSimpleName())) {
            processChildPermissionAnnotation(annotatedClass);
        }
    }

    /**
     * Processes the Plugin Annotation and all known Annotations that may appear
     * on the same Element,
     *
     * @param element The Element annotated with Plugin
     */
    private void processPluginAnnotation(Element element) {
        // Validate the owning Element of the Plugin Annotation
        if (!isPluginAnnotationValid(element)) {
            return;
        }
        // Type validated in above check
        TypeElement mainClass = (TypeElement) element;

        // Plugin fields
        Plugin plugin = mainClass.getAnnotation(Plugin.class);
        data.put(MAIN, mainClass.getQualifiedName().toString());
        data.put(NAME, plugin.name());
        data.put(VERSION, plugin.version());

        // Annotations that may appear on the Plugin annotated Class
        processAnnotation(mainClass, Description.class, DESCRIPTION, Description::value);
        processAnnotation(mainClass, ApiVersion.class, API_VERSION, ApiVersion::value);
        processAnnotation(mainClass, Load.class, LOAD, ann -> ann.value().getLoadType());
        processAnnotation(mainClass, Author.class, AUTHOR, Author::value);
        processAnnotation(mainClass, Authors.class, AUTHORS, Authors::value);
        processAnnotation(mainClass, Website.class, WEBSITE, Website::value);
        processAnnotation(mainClass, Depend.class, DEPEND, Depend::value);
        processAnnotation(mainClass, Prefix.class, PREFIX, Prefix::value);
        processAnnotation(mainClass, SoftDepend.class, SOFT_DEPEND, SoftDepend::value);
        processAnnotation(mainClass, LoadBefore.class, LOAD_BEFORE, LoadBefore::value);
    }

    /**
     * Validates that the Element follows all rules to be annotated with Plugin
     *
     * @param element The Element with the Plugin Annotation
     * @return True if all rules are followed, otherwise false
     */
    private boolean isPluginAnnotationValid(Element element) {
        // Validate inheritance
        TypeMirror javaPluginMirror = this.processingEnv.getElementUtils().getTypeElement(JavaPlugin.class.getName())
            .asType();
        if (!(element instanceof TypeElement typeElement)) {
            raiseError("@Plugin is not on a Class!");
            return false;
        } else if (!this.processingEnv.getTypeUtils().isSubtype(typeElement.asType(), javaPluginMirror)) {
            raiseError("@Plugin is not on a JavaPlugin class!");
            return false;
        } else if (!(typeElement.getEnclosingElement() instanceof PackageElement)) {
            raiseError("@Plugin is not on a top level class!");
            return false;
        }
        // Validate Class modifiers
        Set<Modifier> modifiers = typeElement.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            raiseError("@Plugin cannot be on an Abstract class!");
            return false;
        } else if (modifiers.contains(Modifier.STATIC)) {
            raiseError("@Plugin cannot be on a Static class!");
            return false;
        }
        return true;
    }

    /**
     * Process a simple Annotation to be put in the plugin.yml file
     *
     * @param element        The Element owning the Annotation
     * @param annotationType The Annotation type to process
     * @param tag            The key for the top level YAML data
     * @param handler        Method used to get the data for the key
     */
    private <A extends Annotation> void processAnnotation(Element element, Class<A> annotationType, String tag, YamlValueHandler<A> handler) {
        Optional<A> optional = Optional.ofNullable(element.getAnnotation(annotationType));
        if (optional.isEmpty()) {
            return;
        }
        A annotation = optional.get();
        data.put(tag, handler.getYamlValue(annotation));
    }

    /**
     * Process a collection of CommandTags resulting from a repeated Annotation
     *
     * @param element The Element owning the CommandsTag
     */
    private void processCommandsAnnotation(Element element) {
        Optional<CommandsTag> optional = Optional.ofNullable(element.getAnnotation(CommandsTag.class));
        if (optional.isEmpty()) {
            return;
        }
        for (CommandTag commandTag : optional.get().value()) {
            processCommand(commandTag);
        }
    }

    /**
     * Process a single CommandTag
     *
     * @param element The Element owning the CommandTag
     */
    private void processCommandAnnotation(Element element) {
        Optional<CommandTag> optional = Optional.ofNullable(element.getAnnotation(CommandTag.class));
        if (optional.isEmpty()) {
            return;
        }
        processCommand(optional.get());
    }

    /**
     * Process a CommandTag
     *
     * @param commandTag The Annotation to process
     */
    private void processCommand(CommandTag commandTag) {
        Map<String, Object> block = new HashMap<>();

        // Required
        block.put(DESCRIPTION, commandTag.desc());
        block.put(USAGE, commandTag.usage());
        block.put(PERMISSION, commandTag.permission());

        // Optional
        if (commandTag.aliases().length > 0) {
            block.put(ALIASES, commandTag.aliases());
        }
        if (!commandTag.permissionMessage().equals("")) {
            block.put(PERMISSION_MESSAGE, commandTag.permissionMessage());
        }

        commandsBlock.put(commandTag.name(), block);
    }

    /**
     * Process a collection of PermissionTags resulting from a repeated Annotation
     *
     * @param element The Element owning the PermissionsTag
     */
    private void processPermissionsAnnotation(Element element) {
        Optional<PermissionsTag> optional = Optional.ofNullable(element.getAnnotation(PermissionsTag.class));
        if (optional.isEmpty()) {
            return;
        }
        for (PermissionTag permissionTag : optional.get().value()) {
            processPermission(permissionTag);
        }
    }

    /**
     * Process a single PermissionTag
     *
     * @param element The Element owning the PermissionTag
     */
    private void processPermissionAnnotation(Element element) {
        Optional<PermissionTag> optional = Optional.ofNullable(element.getAnnotation(PermissionTag.class));
        if (optional.isEmpty()) {
            return;
        }
        processPermission(optional.get());
    }

    /**
     * Process a PermissionTag
     *
     * @param permissionTag The Annotation to process
     */
    private void processPermission(PermissionTag permissionTag) {
        Map<String, Object> block = new HashMap<>();

        // Required fields
        block.put(DESCRIPTION, permissionTag.desc());
        block.put(DEFAULT, permissionTag.defaultValue().toString());

        permissionsBlock.put(permissionTag.name(), block);
    }

    /**
     * Process a collection of ChildPermissions resulting from a repeated Annotation
     *
     * @param element The Element owning the ChildPermission
     */
    private void processChildPermissionsAnnotation(Element element) {
        Optional<ChildPermissions> optional = Optional.ofNullable(element.getAnnotation(ChildPermissions.class));
        if (optional.isEmpty()) {
            return;
        }
        for (ChildPermission permissionTag : optional.get().value()) {
            processChildPermission(permissionTag);
        }
    }

    /**
     * Process a single ChildPermission
     *
     * @param element The Element owning the ChildPermission
     */
    private void processChildPermissionAnnotation(Element element) {
        Optional<ChildPermission> optional = Optional.ofNullable(element.getAnnotation(ChildPermission.class));
        if (optional.isEmpty()) {
            return;
        }
        processChildPermission(optional.get());
    }

    /**
     * Process a ChildPermission
     *
     * @param childPermission The Annotation to process
     */
    private void processChildPermission(ChildPermission childPermission) {
        childPermissions.add(childPermission);
    }

    /**
     * Used to fail the annotation processing
     *
     * @param message The message to fail with
     */
    private void raiseError(String message) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

}
