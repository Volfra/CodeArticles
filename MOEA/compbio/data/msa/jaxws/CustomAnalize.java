
package data.msa.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "customAnalize", namespace = "http://msa.data.compbio/01/12/2010/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customAnalize", namespace = "http://msa.data.compbio/01/12/2010/", propOrder = {
    "fastaSequences",
    "options"
})
public class CustomAnalize {

    @XmlElement(name = "fastaSequences", namespace = "")
    private List<data.sequence.FastaSequence> fastaSequences;
    @XmlElement(name = "options", namespace = "")
    private List<metadata.Option> options;

    /**
     * 
     * @return
     *     returns List<FastaSequence>
     */
    public List<data.sequence.FastaSequence> getFastaSequences() {
        return this.fastaSequences;
    }

    /**
     * 
     * @param fastaSequences
     *     the value for the fastaSequences property
     */
    public void setFastaSequences(List<data.sequence.FastaSequence> fastaSequences) {
        this.fastaSequences = fastaSequences;
    }

    /**
     * 
     * @return
     *     returns List<Option>
     */
    public List<metadata.Option> getOptions() {
        return this.options;
    }

    /**
     * 
     * @param options
     *     the value for the options property
     */
    public void setOptions(List<metadata.Option> options) {
        this.options = options;
    }

}
