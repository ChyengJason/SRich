package com.jscheng.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created By Chengjunsen on 2019/3/18
 */
public class AnnotatedClass {
    private static final String CLASS_PAKCAGE = "com.jscheng.processor";
    private static final String CLASS_ROOT_NAME = "Router$$";
    private static final String LOAD_METHOD = "load";
    private static final ClassName IROUTE_INFO = ClassName.get("com.jscheng.srich.route", "IRouteInfo");

    private TypeElement mTypeElement;
    private ArrayList<RouteClass> mRouteClasses;
    private Elements mElements;

    AnnotatedClass(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mRouteClasses = new ArrayList<>();
    }

    void addField(RouteClass field) {
        mRouteClasses.add(field);
    }

    JavaFile generateFile() {
        // 生成参数 Map<String, Class<T>> routes>
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Class.class)
        );

        ParameterSpec parameter = ParameterSpec.builder(parameterizedTypeName, "routes").build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(LOAD_METHOD)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameter)
                .returns(TypeName.VOID);

        for (RouteClass item: mRouteClasses) {
            // uri class
            methodBuilder.addStatement("routes.put($S, $T.class)", item.getUrl(), item.getTypeClass());
        }

        TypeSpec injectClass = TypeSpec.classBuilder(CLASS_ROOT_NAME + mTypeElement.getSimpleName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(IROUTE_INFO)
                .addMethod(methodBuilder.build())
                .build();

        return JavaFile.builder(CLASS_PAKCAGE, injectClass).build();
    }
}
