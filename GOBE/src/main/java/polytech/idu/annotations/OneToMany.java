package polytech.idu.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OneToMany {
    Class<?> target();         // Classe des éléments de la liste
    String mappedBy();         // Nom du champ ManyToOne dans la classe cible
}