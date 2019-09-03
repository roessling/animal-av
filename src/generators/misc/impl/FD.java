package generators.misc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FD {
	private List<Attribute> keys = new ArrayList<>();
	private List<Attribute> values = new ArrayList<>();
	private static int idCounter = 0;
	private int id;

	public FD(Attribute[] keysArray, Attribute[] valuesArray) {
		keys.addAll(Arrays.asList(keysArray));
		values.addAll(Arrays.asList(valuesArray));

		id = idCounter++;
	}
	
	public FD(List<Attribute> keys, List<Attribute> values) {
		this.keys = keys;
		this.values = values;
	}

	public FD(FD fd) {
		keys.addAll(fd.getKeys());
		values.addAll(fd.getValues());

		id = fd.getId();
	}

    public FD() {
    }

    public static FD convertToFD(Relation r) {
		return new FD(r.getPrimaryKey(), r.getAttributes());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Attribute attribute : keys) {
			sb.append(attribute.getSymbol()).append(", ");
		}

		sb.replace(sb.length() - 2, sb.length(), " -> ");

		for (Attribute attribute : values) {
			sb.append(attribute.getSymbol()).append(", ");
		}

		sb.setLength(sb.length() - 2);

		return sb.toString();
	}

    public List<Attribute> getKeys() {
        return this.keys;
    }

    public List<Attribute> getValues() {
        return this.values;
    }

    public int getId() {
        return this.id;
    }

    public void setKeys(List<Attribute> keys) {
        this.keys = keys;
    }

    public void setValues(List<Attribute> values) {
        this.values = values;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FD)) return false;
        final FD other = (FD) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$keys = this.getKeys();
        final Object other$keys = other.getKeys();
        if (this$keys == null ? other$keys != null : !this$keys.equals(other$keys)) return false;
        final Object this$values = this.getValues();
        final Object other$values = other.getValues();
        if (this$values == null ? other$values != null : !this$values.equals(other$values)) return false;
        if (this.getId() != other.getId()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $keys = this.getKeys();
        result = result * PRIME + ($keys == null ? 43 : $keys.hashCode());
        final Object $values = this.getValues();
        result = result * PRIME + ($values == null ? 43 : $values.hashCode());
        result = result * PRIME + this.getId();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof FD;
    }
}
