package com.csse3200.game.components.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;

/**
 * A class used to combine all the data necessary to the individual inventory slots
 */
public class ItemSlot extends Stack {
	private Texture itemTexture;
	private Integer count;
	private final Skin skin = ServiceLocator.getResourceService().getAsset("gardens-of-the-galaxy/gardens-of-the-galaxy.json", Skin.class);
	private Image background;
	private boolean selected;
	private Image itemImage;

	private Label label;
	private Stack draggable;
	private ItemFrame itemFrame;
	static int i;


	/**
	 * Construct an itemSlot with a texture, count and selected state
	 *
	 * @param itemTexture texture of item's image
	 * @param count       count of item
	 * @param selected    boolean state of whether item slot is selected
	 */
	public ItemSlot(Texture itemTexture, Integer count, boolean selected) {
		this.itemTexture = itemTexture;
		this.count = count;
		this.selected = selected;
		this.createItemSlot();
	}

	/**
	 * Construct an itemSlot with a texture and selected state
	 *
	 * @param itemTexture texture of item's image
	 * @param selected    boolean state of whether item slot is selected
	 */
	public ItemSlot(Texture itemTexture, boolean selected) {
		this.itemTexture = itemTexture;
		this.count = null;
		this.selected = selected;
		this.createItemSlot();
	}

	/**
	 * Construct an itemSlot with a selected state
	 *
	 * @param selected boolean state of whether item slot is selected
	 */
	public ItemSlot(boolean selected) {
		this.itemTexture = null;
		this.count = null;
		this.selected = selected;
		this.createItemSlot();

	}

	/**
	 * Set the item count
	 *
	 * @param count integer of number of item
	 */
	public void setCount(Integer count) {
		this.count = count;
		if (this.count > 1) {
			if (label == null) {
				label = new Label(this.count + "", this.skin);
				label.setColor(Color.BLACK);
				label.setAlignment(Align.bottomRight);
				draggable.add(label);
			} else {
				label.setText(this.count + " ");
			}
		} else {
			draggable.removeActor(label);
			if (label != null) {
				this.label = null;
			}
		}
	}

	/**
	 * Get the item count
	 *
	 * @return count integer of number of item
	 */
	public Integer getCount() {
		if (count != null) {
			return count;
		}
		return -1;
	}

	/**
	 * Set the item texture
	 *
	 * @return itemTexture texture of item's image
	 */
	public Texture getItemTexture() {
		return this.itemTexture;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * Creates the itemSlot
	 */
	private void createItemSlot() {
		if (i <= 0) {
			i = 0;
		}
		draggable = new Stack();

		//Add the selection background if necessary
		this.itemFrame = new ItemFrame(this.selected);
		this.add(this.itemFrame);
		//Add the item image to the itemSlot
		if (this.itemTexture != null) {
			itemImage = new Image(this.itemTexture);
			draggable.add(itemImage);
		}

		// Add or update the count label if the number is not 0
		if (this.count != null && this.count > 1) {
			if (label == null) {
				label = new Label(this.count.toString(), this.skin);
				label.setColor(Color.WHITE);
				label.setAlignment(Align.bottomRight);
				draggable.add(label);
			} else {
				label.setText(this.count + " ");
			}
		}
		if (this.count != null && this.count <= 1 && label != null) {
			draggable.removeActor(label);
			this.label = null;
		}

		this.add(draggable);

	}

	/**
	 * Get the item image
	 *
	 * @return the item image
	 */
	public Image getItemImage() {
		return itemImage;
	}

	public Stack getDraggable() {
		return draggable;
	}

	public void setDraggable(Stack stack) {
		this.removeActor(this.draggable);

		if (stack != null) {
			if (stack.getChildren().size == 2) {
				label = (Label) (stack.getChild(1));
			}
			else {
				label = null;
			}
			this.draggable = stack;
			this.add(stack);
		}
	}

	public void setItemImage(Image image) {
		if (draggable.hasChildren()) {
		draggable.removeActorAt(0,true);
		}
		if (image != null) {
			draggable.addActorAt(0, image);
			this.itemImage = image;
		}
	}

	/**
	 * Make the slot selected
	 */
	public void setSelected() {
		this.itemFrame.updateSelected(true);
	}

	/**
	 * Make the slot unselected
	 */
	public void setUnselected() {
		this.itemFrame.updateSelected(false);
	}
}
