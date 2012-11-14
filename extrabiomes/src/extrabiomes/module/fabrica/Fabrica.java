/**
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 3.0 Unported License. To view a copy of this
 * license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package extrabiomes.module.fabrica;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.Item;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.google.common.base.Optional;

import extrabiomes.Extrabiomes;
import extrabiomes.api.Stuff;
import extrabiomes.events.ModuleEvent.ModuleInitEvent;
import extrabiomes.events.ModulePreInitEvent;
import extrabiomes.module.fabrica.block.BlockManager;
import extrabiomes.module.fabrica.recipe.PlankOreDictionaryRecipes;
import extrabiomes.module.fabrica.recipe.RecipeManager;
import extrabiomes.module.fabrica.scarecrow.EntityScarecrow;
import extrabiomes.module.fabrica.scarecrow.ItemScarecrow;

public class Fabrica {

	private int	scarecrowID	= 0;

	@ForgeSubscribe(priority = EventPriority.LOW)
	public void init(ModuleInitEvent event)
			throws InstantiationException, IllegalAccessException
	{
		BlockManager.init();
		PlankOreDictionaryRecipes.init();

		if (scarecrowID > 0) {
			Stuff.scarecrow = Optional
					.of(new ItemScarecrow(scarecrowID)
							.setItemName(ItemScarecrow.NAME).setIconIndex(96));

			final int scarecrowEntityID = Extrabiomes.proxy
					.findGlobalUniqueEntityId();
			Extrabiomes.proxy.registerEntityID(EntityScarecrow.class,
					ItemScarecrow.NAME, scarecrowEntityID);
			Extrabiomes.proxy.registerEntity(EntityScarecrow.class,
					ItemScarecrow.NAME, Extrabiomes.instance, scarecrowEntityID, 300,
					2, true);

			final IRecipe recipe = new ShapedOreRecipe(
					Stuff.scarecrow.get(), new String[] { " p ", "sms",
							" s " }, 'p', Block.pumpkin, 'm',
					Block.melon, 's', Item.stick);
			Extrabiomes.proxy.addRecipe(recipe);
		}
	}

	@ForgeSubscribe(priority = EventPriority.LOW)
	public void preInit(ModulePreInitEvent event)
			throws Exception
	{
		BlockManager.preInit(event.config);
		scarecrowID = event.config.getItem("scarecrow.id",
				Extrabiomes.getNextDefaultItemID()).getInt(0);

		if (scarecrowID > 0)
			Extrabiomes.proxy.registerScarecrowRendering();

		Extrabiomes.registerInitEventHandler(new RecipeManager());
	}

}
