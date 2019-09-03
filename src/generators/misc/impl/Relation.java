package generators.misc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Relation {

	private String name;
	private List<Attribute> primaryKey = new ArrayList<>();
	private List<Attribute> attributes = new ArrayList<>();

	public Relation(String name, Attribute[] primaryKeyArr, Attribute[] attributesArr) {
		this.name = name;
		primaryKey.addAll(Arrays.asList(primaryKeyArr));
		attributes.addAll(Arrays.asList(attributesArr));
	}

	public Relation(String name) {
		this.name = name;
	}

    @java.beans.ConstructorProperties({"name", "primaryKey", "attributes"})
    public Relation(String name, List<Attribute> primaryKey, List<Attribute> attributes) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.attributes = attributes;
    }

    public String getName() {
        return this.name;
    }

    public List<Attribute> getPrimaryKey() {
        return this.primaryKey;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimaryKey(List<Attribute> primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Relation)) return false;
        final Relation other = (Relation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$primaryKey = this.getPrimaryKey();
        final Object other$primaryKey = other.getPrimaryKey();
        if (this$primaryKey == null ? other$primaryKey != null : !this$primaryKey.equals(other$primaryKey))
            return false;
        final Object this$attributes = this.getAttributes();
        final Object other$attributes = other.getAttributes();
        if (this$attributes == null ? other$attributes != null : !this$attributes.equals(other$attributes))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $primaryKey = this.getPrimaryKey();
        result = result * PRIME + ($primaryKey == null ? 43 : $primaryKey.hashCode());
        final Object $attributes = this.getAttributes();
        result = result * PRIME + ($attributes == null ? 43 : $attributes.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Relation;
    }

    public String toString() {
        return "generators.misc.impl.Relation(name=" + this.getName() + ", primaryKey=" + this.getPrimaryKey() + ", attributes=" + this.getAttributes() + ")";
    }
}
