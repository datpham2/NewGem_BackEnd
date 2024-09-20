package project.source.models.enums;

import lombok.Getter;

@Getter
public enum RoomType {
    SINGLE("Single"), DOUBLE("Double"), VIP("VIP");
    private final String type;

    RoomType(String type){
        this.type = type;
    }
}
