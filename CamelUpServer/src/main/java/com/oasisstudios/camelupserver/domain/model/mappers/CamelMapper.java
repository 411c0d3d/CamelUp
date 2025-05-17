package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.Camel;

public class CamelMapper {

    // Convert CamelDOM to Camel
    public static Camel toDTO(CamelDOM camelDOM) {
        if (camelDOM == null) {
            return null;
        }

        Camel camel = new Camel();
        camel.setId(camelDOM.getId());
        camel.setColor(camelDOM.getColor());

        return camel;
    }

    // Convert Camel to CamelDOM
    public static CamelDOM toDomain(Camel camel) {
        if (camel == null) {
            return null;
        }

        CamelDOM.Direction direction;
        if(camel.getId() < 0) {
            direction = CamelDOM.Direction.BACKWARDS;
        }
        else {
            direction = CamelDOM.Direction.FORWARDS;
        }

        return new CamelDOM(camel.getId(), camel.getColor(), direction);
    }

    public static void main(String[] args) {

        // Testing toDTO

        CamelDOM camelDOM = new CamelDOM(2, "FF0000", CamelDOM.Direction.FORWARDS);
        CamelDOM camelDOM1 = new CamelDOM(-1, "0000FF", CamelDOM.Direction.BACKWARDS);

        Camel camel = toDTO(camelDOM);
        Camel camel1 = toDTO(camelDOM1);

        System.out.println("CamelId: " + camel.getId());
        System.out.println("Color: " + camel.getColor());
        System.out.println();
        System.out.println("CamelId: " + camel1.getId());
        System.out.println("Color: " + camel1.getColor());

        // Testing toDomain

        Camel camel2 = new Camel();
        Camel camel3 = new Camel();

        camel2.setId(0);
        camel2.setColor("232323");

        camel3.setId(-2);
        camel3.setColor("424242");

        CamelDOM camelDOM2 = toDomain(camel2);
        CamelDOM camelDOM3 = toDomain(camel3);

        System.out.println();
        System.out.println("CamelId: " + camelDOM2.getId());
        System.out.println("Color: " + camelDOM2.getColor());
        System.out.println("Direction: " + camelDOM2.getDirection());

        System.out.println();
        System.out.println("CamelId: " + camelDOM3.getId());
        System.out.println("Color: " + camelDOM3.getColor());
        System.out.println("Direction: " + camelDOM3.getDirection());

    }

}
