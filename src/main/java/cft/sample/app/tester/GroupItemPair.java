package cft.sample.app.tester;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents pair of group id : item id
 */

@Getter
@Setter
public class GroupItemPair {

    GroupItemPair(Long groupId, Long itemId) {
        this.groupId = groupId;
        this.itemId = itemId;
    }

    private Long groupId;
    private Long itemId;

}
