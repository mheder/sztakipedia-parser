package hu.sztaki.sztakipediaparser.wiki.tags;

import java.util.List;

public interface TreeNode<E extends TreeNode<?>> {

	/**
	 * Adds a single child to this node's children.
	 * 
	 * @param c
	 *            New child.
	 */
	public abstract void addChild(E c);

	/**
	 * Adds the child tag to at the specified index.
	 * 
	 * @param c
	 *            Child tag.
	 * @param index
	 */
	public abstract void addChild(E c, int index);

	/**
	 * Removes a child.
	 * 
	 * @param c
	 *            Child to remove.
	 */
	public abstract void removeChild(E c);

	/**
	 * Returns the index of the specified child or -1 if the child is not found.
	 * 
	 * @param c
	 *            Child tag.
	 * @return Index or -1 if c is not found.
	 */
	public abstract int getChildIndex(E c);

	/***************************/
	/*** Getters and Setters ***/
	/***************************/

	public abstract List<E> getChildren();

	public abstract E getParent();

}