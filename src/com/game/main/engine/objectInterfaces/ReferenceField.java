package com.game.main.engine.objectInterfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by AND0053 on 7/06/2016.
 *
 * This marks the field that should be used to store the objects refrence
 * See IGameObject for more info
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferenceField {
}
