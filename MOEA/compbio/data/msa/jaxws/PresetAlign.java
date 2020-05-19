
package data.msa.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "presetAlign", namespace = "http://msa.data.compbio/01/01/2010/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "presetAlign", namespace = "http://msa.data.compbio/01/01/2010/", propOrder = {
    "fastaSequences",
    "preset"
})
public class PresetAlign {

    @XmlElement(name = "fastaSequences", namespace = "")
    private List<data.sequence.FastaSequence> fastaSequences;
    @XmlElement(name = "preset", namespace = "")
    private metadata.Preset preset;

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
     *     returns Preset
     */
    public metadata.Preset getPreset() {
        return this.preset;
    }

    /**
     * 
     * @param preset
     *     the value for the preset property
     */
    public void setPreset(metadata.Preset preset) {
        this.preset = preset;
    }

}
