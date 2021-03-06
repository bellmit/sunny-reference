import com.anicloud.sunny.infrastructure.persistence.domain.share.TriggerType;
import org.junit.Test;

import java.util.EnumSet;

/**
 * Created by zhaoyu on 15-5-26.
 */
public class TestApp {
    @Test
    public void testApp() {
        EnumSet<TriggerType> triggerTypeEnumSet = EnumSet.allOf(TriggerType.class);
        for (TriggerType triggerType : triggerTypeEnumSet) {
            System.out.println(triggerType.ordinal() + " : " + triggerType.name() + " : " + triggerType.toString());
        }
    }

}
