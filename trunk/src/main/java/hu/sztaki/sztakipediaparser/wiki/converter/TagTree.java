package hu.sztaki.sztakipediaparser.wiki.converter;

import hu.sztaki.sztakipediaparser.wiki.tags.BodyTag;
import hu.sztaki.sztakipediaparser.wiki.tags.Tag;

import java.util.Stack;

public class TagTree {
	/**
	 * The top node of the tag stack is the actual node. If adding a new node to
	 * the tree, the actual node will be its parent. If the new node is not a
	 * terminator node, i.e. it can have children, then the actual node is
	 * checked first. If the actual node equals the new node, then the tag stack
	 * is reduced and no new node will be added to the tree. (A terminator node
	 * can be an external link because it doesn't have any children in wikitext
	 * sense. However, a pair of ''' bold nodes can have any other nodes between
	 * them.)
	 */
	private Stack<Tag> tagStack;
	/**
	 * Tree root element storing the tags.
	 */
	private Tag treeRoot;

	public TagTree() {
		tagStack = new Stack<Tag>();
		treeRoot = new BodyTag();
	}

	/**
	 * Returns the top element of the stack, or the root node of the tree if the
	 * stack is empty.
	 * 
	 * @return Top element of tag stack or the root node of the tag tree.
	 */
	public Tag reduceTagStack() {
		if (tagStack.isEmpty()) {
			return treeRoot;
		}

		return tagStack.pop();
	}

	/**
	 * Returns the top element of the stack without removing it or the tree if
	 * the stack is empty.
	 * 
	 * @return Top element of tag stack or the root node of the tag tree.
	 */
	public Tag peekTagStack() {
		if (tagStack.isEmpty()) {
			return treeRoot;
		}

		return tagStack.peek();
	}

	/**
	 * Pushes a new Tag to the tag stack.
	 * 
	 * @param tag
	 */
	public void pushToStack(Tag tag) {
		tagStack.push(tag);
	}

	/**
	 * clears the tag stack
	 */
	public void reset() {
		tagStack.clear();
	}

	/**
	 * Clears the tag stack, and replaces the root element of the tree
	 */
	public void reInitialize() {
		reset();
		treeRoot = new BodyTag();
	}

	public Tag getTreeRoot() {
		return treeRoot;
	}

}
