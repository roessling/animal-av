package generators.misc.id3_chi_squared;

import java.util.List;

/** Date
 * contains a list of literals and a label containing the class of the date
 * all Date instances in one data set need the same amount of literals
 */
public class Date {

    private String label;
    private List<String> literals;

    public Date(String label, List<String> literals){
        this.label = label;
        this.literals = literals;
    }

    /** removeAttribute
     * is called in ID3.cloneDataSet()
     *
     * @param i index of attribute/literal which needs to be removed
     */
    public void removeAttribute(int i){
        literals.remove(i);
    }

    public String getLabel(){return label;}
    public List<String> getLiterals(){return literals;}
}
