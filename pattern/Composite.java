package br.com.florencio.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Composite implements Serializable {
	private static final long serialVersionUID = 1214102220657826946L;
	private final List<Composite> childs;
	private Composite parent;
	
	public Composite() {
		childs = new ArrayList<>();
	}
	
	public void add(Composite c) {
		if(c.parent != null) {
			c.parent.remove(c);
		}

		Composite cmp = this;
		
		while(cmp != null) {
			if(c == cmp) {
				throw new IllegalStateException();
			}
		
			cmp = cmp.parent;
		}
		
		c.parent = this;
		childs.add(c);
	}
	
	public void remove(Composite c) {
		if(c.parent != this) {
			return;
		}
		
		c.parent = null;
		childs.remove(c);
	}

	public List<Composite> getChilds() {
		return Collections.unmodifiableList(childs);
	}

	public Composite getChild(int index) {
		if(index < 0 || index >= getSize()) {
			throw new IllegalStateException();
		}
		
		return childs.get(index);
	}
	
	public int getSize() {
		return childs.size();
	}
	
	public boolean isEmpty() {
		return childs.isEmpty();
	}	
}