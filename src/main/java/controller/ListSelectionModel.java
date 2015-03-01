package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Florian Hug <florian.hug@gmail.com> on 2/28/15.
 */
public class ListSelectionModel<T> extends MultipleSelectionModel<T> {

    private final ArrayList<T> items;
    private final ObservableList<T> observableItems;
    private final ObservableList<Integer> observableIndices;

    public ListSelectionModel(final ArrayList<T> items) {
        this.items = items;
        this.observableIndices = FXCollections.observableList(new LinkedList<>());
        observableItems = FXCollections.observableList(new LinkedList<>());
    }

    public Set<T> getSelected() {
        return this.observableItems.stream().collect(Collectors.toSet());
    }

    /**
     * <p>Returns a <b>read-only</b> ObservableList of all selected indices. The
     * ObservableList will be updated  by the selection model to always reflect
     * changes in selection. This can be observed by adding a
     * {@link javafx.collections.ListChangeListener} to the returned ObservableList.
     */
    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return this.observableIndices;
    }

    /**
     * <p>Returns a <b>read-only</b> ObservableList of all selected items. The
     * ObservableList will be updated further by the selection model to always reflect
     * changes in selection. This can be observed by adding a
     * {@link javafx.collections.ListChangeListener} to the returned ObservableList.
     */
    @Override
    public ObservableList<T> getSelectedItems() {
        return this.observableItems;
    }

    /**
     * <p>This method allows for one or more selections to be set at the same time.
     * It will ignore any value that is not within the valid range (i.e. greater
     * than or equal to zero, and less than the total number of items in the
     * underlying data model). Any duplication of indices will be ignored.
     * <p>
     * <p>If there is already one or more indices selected in this model, calling
     * this method will <b>not</b> clear these selections - to do so it is
     * necessary to first call clearSelection.
     * <p>
     * <p>The last valid value given will become the selected index / selected
     * item.
     *
     * @param index
     * @param indices
     */
    @Override
    public void selectIndices(int index, int... indices) {
        this.select(index);
        for (int i : indices) {
            this.select(i);
        }
    }

    /**
     * <p>Convenience method to select all available indices.</p>
     */
    @Override
    public void selectAll() {
        this.items.stream().forEach(this.observableItems::add);
        for (int i = 0; i < this.items.size(); ++i) {
            this.observableIndices.add(i);
        }
    }

    /**
     * A method that clears any selection prior to setting the selection to the
     * given index. The purpose of this method is to avoid having to call
     * {@link #clearSelection()} first, meaning that observers that are listening to
     * the {@link #selectedIndexProperty() selected index} property will not
     * see the selected index being temporarily set to -1.
     *
     * @param index The index that should be the only selected index in this
     *              selection model.
     */
    @Override
    public void clearAndSelect(int index) {
        this.clearSelection();
        this.select(index);
    }

    /**
     * <p>This will select the given index in the selection model, assuming the
     * index is within the valid range (i.e. greater than or equal to zero, and
     * less than the total number of items in the underlying data model).
     * <p>
     * <p>If there is already one or more indices selected in this model, calling
     * this method will <b>not</b> clear these selections - to do so it is
     * necessary to first call {@link #clearSelection()}.
     * <p>
     * <p>If the index is already selected, it will not be selected again, or
     * unselected. However, if multiple selection is implemented, then calling
     * select on an already selected index will have the effect of making the index
     * the new selected index (as returned by {@link #getSelectedIndex()}.
     *
     * @param index The position of the item to select in the selection model.
     */
    @Override
    public void select(int index) {
        if (index >= 0) {
            this.observableIndices.add(index);
            this.observableItems.add(this.items.get(index));
        }
    }

    /**
     * <p>This method will attempt to select the index that contains the given
     * object. It will iterate through the underlying data model until it finds
     * an item whose value is equal to the given object. At this point it will
     * stop iterating - this means that this method will not select multiple
     * indices.
     *
     * @param obj The object to attempt to select in the underlying data model.
     */
    @Override
    public void select(T obj) {
        this.observableItems.add(obj);
        int index = this.items.indexOf(obj);
        if (index >= 0) {
            this.observableIndices.add(index);
        }
    }

    /**
     * <p>This method will clear the selection of the item in the given index.
     * If the given index is not selected, nothing will happen.
     *
     * @param index The selected item to deselect.
     */
    @Override
    public void clearSelection(int index) {
        if (this.observableIndices.contains(index)) {
            this.observableIndices.remove((Object) index);
        }
        this.observableItems.remove(this.items.get(index));
    }

    /**
     * <p>Clears the selection model of all selected indices.
     */
    @Override
    public void clearSelection() {
        this.observableItems.clear();
        this.observableIndices.clear();
    }

    /**
     * <p>Convenience method to inform if the given index is currently selected
     * in this SelectionModel. Is functionally equivalent to calling
     * <code>getSelectedIndices().contains(index)</code>.
     *
     * @param index The index to check as to whether it is currently selected
     *              or not.
     * @return True if the given index is selected, false otherwise.
     */
    @Override
    public boolean isSelected(int index) {
        return this.observableIndices
                .stream()
                .filter(item -> item == index)
                .findAny()
                .isPresent();
    }

    /**
     * This method is available to test whether there are any selected
     * indices/items. It will return true if there are <b>no</b> selected items,
     * and false if there are.
     *
     * @return Will return true if there are <b>no</b> selected items, and false
     * if there are.
     */
    @Override
    public boolean isEmpty() {
        return this.observableIndices.isEmpty();
    }

    /**
     * <p>This method will attempt to select the index directly before the current
     * focused index. If clearSelection is not called first, this method
     * will have the result of selecting the previous index, whilst retaining
     * the selection of any other currently selected indices.</p>
     * <p>
     * <p>Calling this method will only succeed if:</p>
     * <p>
     * <ul>
     * <li>There is currently a lead/focused index.
     * <li>The lead/focus index is not the first index in the control.
     * <li>The previous index is not already selected.
     * </ul>
     * <p>
     * <p>If any of these conditions is false, no selection event will take
     * place.</p>
     */
    @Override
    public void selectPrevious() {
        throw new NotImplementedException();
    }

    /**
     * <p>This method will attempt to select the index directly after the current
     * focused index. If clearSelection is not called first, this method
     * will have the result of selecting the next index, whilst retaining
     * the selection of any other currently selected indices.</p>
     * <p>
     * <p>Calling this method will only succeed if:</p>
     * <p>
     * <ul>
     * <li>There is currently a lead/focused index.
     * <li>The lead/focus index is not the last index in the control.
     * <li>The next index is not already selected.
     * </ul>
     * <p>
     * <p>If any of these conditions is false, no selection event will take
     * place.</p>
     */
    @Override
    public void selectNext() {
        throw new NotImplementedException();
    }

    /**
     * <p>This method will attempt to select the first index in the control. If
     * clearSelection is not called first, this method
     * will have the result of selecting the first index, whilst retaining
     * the selection of any other currently selected indices.</p>
     * <p>
     * <p>If the first index is already selected, calling this method will have
     * no result, and no selection event will take place.</p>
     */
    @Override
    public void selectFirst() {
        throw new NotImplementedException();
    }

    /**
     * <p>This method will attempt to select the last index in the control. If
     * clearSelection is not called first, this method
     * will have the result of selecting the last index, whilst retaining
     * the selection of any other currently selected indices.</p>
     * <p>
     * <p>If the last index is already selected, calling this method will have
     * no result, and no selection event will take place.</p>
     */
    @Override
    public void selectLast() {
        throw new NotImplementedException();
    }
}
