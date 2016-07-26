package api.simplelib.sitting;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

/**
 * This interface representing a seat.
 * <p>If this the class implementing this interface and annotated by {@link ModSeat}, this class will be registered.</p>
 *
 * @author ci010
 */
public interface Sitable
{
	/**
	 * @return The block you want to be sitable.
	 */
	Block sitableBlock();

	/**
	 * @return The specific sitting situation.
	 */
	Situation getSituation();

	/**
	 * The situation that sitting need to fallow.
	 */
	interface Situation
	{
		/**
		 * Decide if a player should sit on a specific position
		 *
		 * @param player The player will be sit.
		 * @param block  The position of the block.
		 * @return if a player can sit on.
		 */
		boolean shouldSit(EntityPlayer player, BlockPos block);

		/**
		 * @return The vertical offset of the seat.
		 */
		float offsetVertical();

		/**
		 * @return The horizontal offset of the seat.
		 */
		float offsetHorizontal();
	}

	/**
	 * This situation should be available for all the stairs.
	 */
	Situation DEFAULT = new Situation()
	{
		@Override
		public boolean shouldSit(EntityPlayer player, BlockPos block)
		{
			return player.getHeldItem(EnumHand.MAIN_HAND) == null;
		}

		@Override
		public float offsetVertical()
		{
			return 0;
		}

		@Override
		public float offsetHorizontal()
		{
			return 0.25f;
		}
	};
}
