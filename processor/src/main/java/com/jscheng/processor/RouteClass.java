package com.jscheng.processor;

import com.jscheng.annotations.Route;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created By Chengjunsen on 2019/3/18
 */
public class RouteClass {
    private TypeElement mTypeElement;
    private String mUrl;

    RouteClass(TypeElement element) throws IllegalArgumentException {

        if (element.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(String.format("Only classes can be annotated with @%s",
                    Route.class.getSimpleName()));
        }

        mTypeElement = element;

        Route route = mTypeElement.getAnnotation(Route.class);
        mUrl = route.value();

        if (mUrl.isEmpty()) {
            throw new IllegalArgumentException(String.format("value() in %s for class %s is not valid !",
                    Route.class.getSimpleName(),
                    mTypeElement.getSimpleName()));
        }
    }


    TypeElement getTypeClass() {
        return mTypeElement;
    }

    String getUrl() {
        return mUrl;
    }

    TypeMirror getFieldType() {
        return mTypeElement.asType();
    }

    public Name getClassName() {
        return mTypeElement.getSimpleName();
    }
}
