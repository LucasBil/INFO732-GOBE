package polytech.idu.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToOne {
    Class<?> target();        // Classe cible
    String columnName();      // Nom de la colonne FK dans CE modèle
    String refColumn() default "id"; // Colonne du modèle cible
    String onDelete() default "CASCADE";
    String onUpdate() default "CASCADE";
}
