package org.processmining.plugins.inductiveminer2.attributes;

public abstract class AttributeAbstract implements Attribute {

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(Attribute arg0) {
		return getName().toLowerCase().compareTo(arg0.getName().toLowerCase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().toLowerCase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeImpl other = (AttributeImpl) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().toLowerCase().equals(other.getName().toLowerCase()))
			return false;
		return true;
	}

}