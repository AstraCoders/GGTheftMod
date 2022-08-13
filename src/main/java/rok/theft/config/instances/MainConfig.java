package rok.theft.config.instances;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
 * @author Rok, Pedro Lucas N M Machado created on 13/08/2022
 */
@Data
public class MainConfig {
		private boolean DamageItem = true;
		private int itemDurability = 16;
		private boolean stealTimer = true;
		private int stealTime = 10;
		// Think some more
}
