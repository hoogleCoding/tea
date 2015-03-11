package model;

import javax.money.CurrencyUnit;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 10/20/14.
 */
public class Account {
    private final Long id;
    private String name;
    private String description;
    private Long creationTimestamp;
    private CurrencyUnit currency;

    public Account(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Account() {
        this.id = null;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setCreationTimestamp(final Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Account that = (Account) o;

        return Objects.equals(this.id, that.id) ||
                (Objects.equals(this.name, that.name) && Objects.equals(this.creationTimestamp, that.creationTimestamp));

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.creationTimestamp, this.name);
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Optional<CurrencyUnit> getCurrency() {
        return Optional.ofNullable(currency);
    }

    public void setCurrency(CurrencyUnit currency) {
        this.currency = currency;
    }
}
