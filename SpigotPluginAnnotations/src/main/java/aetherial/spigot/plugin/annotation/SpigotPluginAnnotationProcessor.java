package aetherial.spigot.plugin.annotation;

import aetherial.spigot.plugin.annotation.command.CommandTag;
import aetherial.spigot.plugin.annotation.command.CommandsTag;
import aetherial.spigot.plugin.annotation.permission.ChildPermission;
import aetherial.spigot.plugin.annotation.permission.PermissionTag;
import aetherial.spigot.plugin.annotation.permission.PermissionsTag;
import aetherial.spigot.plugin.annotation.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SupportedAnnotationTypes("aetherial.spigot.plugin.annotation.*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class SpigotPluginAnnotationProcessor extends AbstractProcessor {

    private boolean foundMainClass = false;

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
            return true;
        }

        if (!commandsBlock.isEmpty()) {
            data.put(COMMANDS, commandsBlock);
        }
        if (!permissionsBlock.isEmpty()) {
            data.put(PERMISSIONS, permissionsBlock);
        }

        try (Writer writer = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE)
            .openWriter()) {
            Yaml yaml = new Yaml();
            String raw = yaml.dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
            writer.write(raw);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            raiseError("Failed to create plugin.yml file");
            return false;
        }

        return true;
    }

    private void processAnnotation(Element annotatedClass, TypeElement annotation) {
        String annotationName = annotation.asType().toString();
        annotationName = annotationName.substring(annotationName.lastIndexOf('.') + 1);

        if (annotationName.equals(Plugin.class.getSimpleName())) {
            if (foundMainClass) {
                raiseError("Already found a main class annotated with @Plugin! Aborting, more than one class with @Plugin");
                return;
            }
            foundMainClass = true;
            processPluginAnnotation(annotatedClass);
        } else if (annotationName.equals(CommandsTag.class.getSimpleName())) {
            processCommandsAnnotation(annotatedClass);
        } else if (annotationName.equals(CommandTag.class.getSimpleName())) {
            processCommandAnnotation(annotatedClass);
        } else if (annotationName.equals(PermissionsTag.class.getSimpleName())) {
            processPermissionsAnnotation(annotatedClass);
        } else if (annotationName.equals(PermissionTag.class.getSimpleName())) {
            processPermissionAnnotation(annotatedClass);
        }
    }

    private void processPluginAnnotation(Element mainClassElement) {
        TypeMirror javaPluginMirror = this.processingEnv.getElementUtils().getTypeElement(JavaPlugin.class.getName()).asType();
        if (!(mainClassElement instanceof TypeElement mainClass)) {
            raiseError("@Plugin is not on a Class!");
            return;
        } else if (!this.isSubtype(mainClass.asType(), javaPluginMirror)) {
            raiseError("@Plugin is not on a JavaPlugin class!");
            return;
        } else if (!(mainClass.getEnclosingElement() instanceof PackageElement)) {
            raiseError("@Plugin is not on a top level class!");
            return;
        }
        Set<Modifier> modifiers = mainClass.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            raiseError("@Plugin cannot be on an Abstract class!");
            return;
        } else if (modifiers.contains(Modifier.STATIC)) {
            raiseError("@Plugin cannot be on a Static class!");
            return;
        }


        Plugin plugin = mainClassElement.getAnnotation(Plugin.class);
        data.put(MAIN, mainClass.getQualifiedName().toString());
        data.put(NAME, plugin.name());
        data.put(VERSION, plugin.version());

        processDescriptionAnnotation(mainClassElement);
        processApiVersionAnnotation(mainClassElement);
        processLoadAnnotation(mainClassElement);
        processAuthorAnnotation(mainClassElement);
        processAuthorsAnnotation(mainClassElement);
        processWebsiteAnnotation(mainClassElement);
        processDependAnnotation(mainClassElement);
        processPrefixAnnotation(mainClassElement);
        processSoftDependAnnotation(mainClassElement);
        processLoadBeforeAnnotation(mainClassElement);
    }

    private void processDescriptionAnnotation(Element mainClassElement) {
        Optional<Description> optional = Optional.ofNullable(mainClassElement.getAnnotation(Description.class));
        if (optional.isEmpty()) {
            return;
        }
        Description annotation = optional.get();
        data.put(DESCRIPTION, annotation.value());
    }

    private void processApiVersionAnnotation(Element mainClassElement) {
        Optional<ApiVersion> optional = Optional.ofNullable(mainClassElement.getAnnotation(ApiVersion.class));
        if (optional.isEmpty()) {
            return;
        }
        ApiVersion annotation = optional.get();
        data.put(API_VERSION, annotation.value());
    }

    private void processLoadAnnotation(Element mainClassElement) {
        Optional<Load> optional = Optional.ofNullable(mainClassElement.getAnnotation(Load.class));
        if (optional.isEmpty()) {
            return;
        }
        Load annotation = optional.get();
        data.put(LOAD, annotation.value().getLoadType());
    }

    private void processAuthorAnnotation(Element mainClassElement) {
        Optional<Author> optional = Optional.ofNullable(mainClassElement.getAnnotation(Author.class));
        if (optional.isEmpty()) {
            return;
        }
        Author annotation = optional.get();
        data.put(AUTHOR, annotation.value());
    }

    private void processAuthorsAnnotation(Element mainClassElement) {
        Optional<Authors> optional = Optional.ofNullable(mainClassElement.getAnnotation(Authors.class));
        if (optional.isEmpty()) {
            return;
        }
        Authors annotation = optional.get();
        data.put(AUTHORS, annotation.value());
    }

    private void processWebsiteAnnotation(Element mainClassElement) {
        Optional<Website> optional = Optional.ofNullable(mainClassElement.getAnnotation(Website.class));
        if (optional.isEmpty()) {
            return;
        }
        Website annotation = optional.get();
        data.put(WEBSITE, annotation.value());
    }

    private void processDependAnnotation(Element mainClassElement) {
        Optional<Depend> optional = Optional.ofNullable(mainClassElement.getAnnotation(Depend.class));
        if (optional.isEmpty()) {
            return;
        }
        Depend annotation = optional.get();
        data.put(DEPEND, annotation.value());
    }

    private void processPrefixAnnotation(Element mainClassElement) {
        Optional<Prefix> optional = Optional.ofNullable(mainClassElement.getAnnotation(Prefix.class));
        if (optional.isEmpty()) {
            return;
        }
        Prefix annotation = optional.get();
        data.put(PREFIX, annotation.value());
    }

    private void processSoftDependAnnotation(Element mainClassElement) {
        Optional<SoftDepend> optional = Optional.ofNullable(mainClassElement.getAnnotation(SoftDepend.class));
        if (optional.isEmpty()) {
            return;
        }
        SoftDepend annotation = optional.get();
        data.put(SOFT_DEPEND, annotation.value());
    }

    private void processLoadBeforeAnnotation(Element mainClassElement) {
        Optional<LoadBefore> optional = Optional.ofNullable(mainClassElement.getAnnotation(LoadBefore.class));
        if (optional.isEmpty()) {
            return;
        }
        LoadBefore annotation = optional.get();
        data.put(LOAD_BEFORE, annotation.value());
    }

    private boolean isSubtype(TypeMirror child, TypeMirror parent) {
        return this.processingEnv.getTypeUtils().isSubtype(child, parent);
    }

    private void processCommandsAnnotation(Element classElement) {
        Optional<CommandsTag> optional = Optional.ofNullable(classElement.getAnnotation(CommandsTag.class));
        if (optional.isEmpty()) {
            return;
        }
        for (CommandTag commandTag : optional.get().value()) {
            processCommand(commandTag);
        }
    }

    private void processCommandAnnotation(Element classElement) {
        Optional<CommandTag> optional = Optional.ofNullable(classElement.getAnnotation(CommandTag.class));
        if (optional.isEmpty()) {
            return;
        }
        processCommand(optional.get());
    }

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

    private void processPermissionsAnnotation(Element classElement) {
        Optional<PermissionsTag> optional = Optional.ofNullable(classElement.getAnnotation(PermissionsTag.class));
        if (optional.isEmpty()) {
            return;
        }
        for (PermissionTag permissionTag : optional.get().value()) {
            processPermission(permissionTag);
        }
    }

    private void processPermissionAnnotation(Element classElement) {
        Optional<PermissionTag> optional = Optional.ofNullable(classElement.getAnnotation(PermissionTag.class));
        if (optional.isEmpty()) {
            return;
        }
        processPermission(optional.get());
    }

    private void processPermission(PermissionTag permissionTag) {
        Map<String, Object> block = new HashMap<>();
        Map<String, Object> childrenBlock = new HashMap<>();
        block.put(DESCRIPTION, permissionTag.desc());
        block.put(DEFAULT, permissionTag.defaultValue().toString());

        if (permissionTag.children().length > 0) {
            for (ChildPermission child : permissionTag.children()) {
                childrenBlock.put(child.name(), child.inherit());
            }
            block.put(CHILDREN, childrenBlock);
        }

        permissionsBlock.put(permissionTag.name(), block);
    }

    private void raiseError(String message) {
        this.processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR, message);
    }

}
