package generators.hardware.prefixAdderAnimation.util;

/**
 * Created by philipp on 02.05.15.
 * Holds all longer texts that are displayed during animation
 */
public class MyText {
    public static final String INTRODUCTION =
            "A prefix adder is a special kind of binary adder. One of the main problems of other types \n" +
            "of adders is that one needs to compute each bit sequentially since for the computation of \n" +
            "bit i one needs to know whether or not there is a carry from bit i-1, this  \n" +
            "yields a linear time complexity. \n \n" +
            "Prefix adders overcome this problem by using certain logic to precompute the carry bits  \n" +
            "for each bit and archive a logarithmic time complexity. Of course this advantage comes \n" +
            "at a price, and as it is often with hardware this price comes in the form of additional \n"+
            "hardware (transistors) and hence increased space and power consumption. \n \n"+
            "This animation first shows how the adder works in general and then performs an exemplary \n" +
            "computation. \n \n";

    public static final String ADDITIONAL_INTRO_FOR_GEN =
            "This generator generates a animation of a 8 bit prefix adder and supports addition and \n " +
            "subtraction, both with either signed and unsigned binary numbers. Signed values are \n "+
            "represented in two's complement representation.\n" +
            "The (decimal) input values should be chosen accordingly: \n \n "+
            "      - Signed  : Values between -128 and 127 \n" +
            "      - Unsigned: Values between 0 and 255. For subtraction only: A greater or equal to B \n" +
            "Possible overflows are detected! \n \n"+
            "Remark: One may find prefix adders in literature and practice whose design differ \n" +
            "from the one displayed here. This particular adder is taken from 'Digital Design and Computer \n"+
            "Architecture' by Harris and Harris";




    public static final String DESCRIPTION_FIRST_PAGE =
            "Each prefix consists of two signals, a propagate and a generate signal. As indicated \n " +
            "by the names they signal whether a carry is generated and/or propagated. \n \n"+
            "First in the 'input layer' the first prefixes are computed from the input data.\n \n" +
            "Afterwards in the 'internal Layers' two prefixes are combined to form a prefix  \n " +
            "that covers twice as many bits, this is done until each prefix covers all necessary\n"+
            "bits(all bits of lower significance).\n \n"+
            "Finally the generate signals of the last prefixes and the input data are combined \n " +
            "like they would be combined in an ordinary 1 bit full adder.";
    //SourceCode for Blocks
    // Block descriptions
    public static final String DESCRIPTION_INPUT_BLOCK =
           "The input blocks first compute the generate \n and propagate signal from the input data: \n" +
           "       - The generate signal is 1 if A and B are 1 \n"+
           "       - The propagate signal is 1 if A or B is 1 ";

    public static final String DESCRIPTION_INTERNAL_BLOCK[] = {
            "The internal block now combine the propagate and generate block \n " +
            "of two blocks from the previous layers\n"+
            "(we will see later how the blocks are combined - for now we will\n " +
            " just call them the upper (P_u and G_u) and lower block (P_l and G_l)",
            "       - The propagate signal is 1 if both for both input blocks \n " +
            "         the propagate signal is 1",
            "       - The generate signal is 1 if the upper part propagates and \n" +
            "         the lower generates a signal or the upper generates a signal\n" +
            "         itself"};

    public static final String DESCRIPTION_OUTPUT_BLOCK =
            "Finally, the output blocks combine the generate signals from the last \n" +
            "internal block and the inputs. \n"+
            "This is done like in a normal adder: With a XOR of the inputs and the \n" +
            "generate (carry) signal \n";

    public static final String DESCRIPTION_CARRY_IN =
            "Carry Input: \n"+
            "Generate can be set to true, either to propagate a carry from \n" +
            "a previous adder (not part of this animation) or for  \n" +
            "the '+ 1' needed for subtraction in two's-complement. \n \n"+
            "Propagate is always false!";

    public static final String DESCRIPTION_WIRING =
            "Each line represents a wire for the generate as well as the propagate signal. \n"+
            "The wires for the actual inputs A and B are not displayed.";

    public static final String DESCRIPTION_LUEXAMPLE_1 =
            "Consider the highlighted blocks: \n"+
            "       - The block marked green is the lower input block \n" +
            "       - The block marked blue is the upper input block \n"+
            "One sees now that the terms 'upper' and 'lower' refer to the significance of \n"+
            "the single bits (in deeper layers one consider ranges of bits that are already\n"+
            "combined).";

    public static final String DESCRIPTION_LUEXAMPLE_2 =
            "Again consider the highlighted blocks \n"+
            "       - The block marked green is the lower input block\n" +
            "       - The block marked blue is the upper input block \n" +
            "This is an example for how the terms 'lower' and 'upper' refer to a range of bits.";

    public static final String CONCLUSION =
            "In the animation it may seemed that the addition took longer than with a normal \n"+
            "adder, but keep in mind that in practice each layer computes the values of all \n"+
            "Blocks in parallel (and thus archives logarithmic complexity). This effect \n " +
            "is seen even better considering examples that are not toy sized like in the animation. \n \n" +
            "In fact this timing advantage is significant enough that modern CPUs (as well as \n" +
            "GPUs and most other chips) exclusively use prefix adders, despite the disadvantageous \n"+
            "factors such as increased space and energy consumption caused by the additional \n" +
            "hardware.";

    public static final String inputPForm = "P = A or B";
    public static final String inputGForm = "G = A and B";





}