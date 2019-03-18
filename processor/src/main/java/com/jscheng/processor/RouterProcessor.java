package com.jscheng.processor;

import com.google.auto.service.AutoService;
import com.jscheng.annotations.Route;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created By Chengjunsen on 2019/3/18
 */
@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    private Filer mFiler;

    private Elements mElements;

    private Messager mMessager;

    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        mAnnotatedClassMap = new TreeMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mAnnotatedClassMap.clear();

        processRoute(roundEnvironment);

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                JavaFile javaFile = annotatedClass.generateFile();
                info(javaFile.toString());
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }

    private void processRoute(RoundEnvironment roundEnv) throws IllegalArgumentException {
        for (Element element : roundEnv.getElementsAnnotatedWith(Route.class)) {
            TypeElement typeElement = (TypeElement) element;
            PackageElement packageElement = (PackageElement) element.getEnclosingElement();

            String fullName = typeElement.getQualifiedName().toString();

            AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);

            if (annotatedClass == null) {
                annotatedClass = new AnnotatedClass(typeElement, mElements);
                mAnnotatedClassMap.put(fullName, annotatedClass);
            }

            RouteClass routeClass = new RouteClass(typeElement);
            info(routeClass.getClassName() + " " + routeClass.getUrl());
            annotatedClass.addField(routeClass);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new LinkedHashSet<>();
        set.add(Route.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
