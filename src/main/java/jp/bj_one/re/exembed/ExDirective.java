package jp.bj_one.re.exembed;

import java.io.Serializable;
import java.util.Objects;

final class ExDirective implements Comparable<ExDirective>, Serializable {
	private static final long serialVersionUID = 1L;

	final String directive;
	
	enum Type {
		BASIC_TYPE,
		ARRAY_TYPE,
		NO_INDEX_ARRAY_TYPE
	};
	final Type type;
	
	final Integer index;

	ExDirective(String directive, Type type, int index) {
		this.directive = directive;
		this.type = type;
		if (type == Type.ARRAY_TYPE)
			this.index = index;
		else
			this.index = -1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(directive, index, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExDirective other = (ExDirective) obj;
		return Objects.equals(directive, other.directive) && index == other.index && type == other.type;
	}

	@Override
	public int compareTo(ExDirective o) {
		int result = this.directive.compareTo(o.directive);
		if (result != 0)
			return result;
		result = this.type.compareTo(o.type);
		if (result != 0)
			return result;
		return this.index.compareTo(o.index);
	}
	
	public String toString() {
		switch (this.type){
			case BASIC_TYPE:
				return this.directive;
			case ARRAY_TYPE:
				return this.directive + "[" + this.index.toString() + "]";
			case NO_INDEX_ARRAY_TYPE:
				return this.directive + "[]";
			default:
				return "";	
		}
	}
}
