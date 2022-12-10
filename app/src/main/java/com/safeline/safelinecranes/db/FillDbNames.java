package com.safeline.safelinecranes.db;

import com.safeline.safelinecranes.models.Answer;
import com.safeline.safelinecranes.models.OutcomeCodes;
import com.safeline.safelinecranes.models.Question;

import java.util.ArrayList;
import java.util.List;

public final class FillDbNames {

    private FillDbNames() {}

    public static List<Question> fillDBWithQuestions() {
        List<Question> questions = new ArrayList<>();

        /* WIRE QUESTIONS */
        Question q1 = new Question(31,"ABRASION", "Which is the condition of the rope after its usage due to abrasion?", "WIRE", true, "WIRE",
                "RISK", 10, null, null, "red");
        questions.add(q1);
        Question q2 = new Question(32,"CROWN BROKEN WIRES (BENDING EFFECT 1)", "Which is the condition of the wire rope after its usage due to crown broken wires?", "WIRE", true, "WIRE",
                "RISK", 20, null, null, "red");
        questions.add(q2);
        Question q3 = new Question(33,"VALLEY / TERMINATION BROKEN WIRES / STRAND (BENDING EFFECT 2)", "Which is the condition of the wire rope after its usage due to valley / termination broken wires?", "WIRE", true, "WIRE",
                "RISK", 30, null, null, "red");
        questions.add(q3);
        Question q4 = new Question(34,"DIAMETER DECREASE", "Which is the condition of the wire rope – uniform diameter decrease- after its usage due to external wear / abrasion / internal wear / core deterioration?", "WIRE", true, "WIRE",
                "RISK", 40, null, null, "red");
        questions.add(q4);
        Question q5 = new Question(35,"DIAMETER INCREASE", "Has the wire rope exhibited local increase in diameter?", "WIRE", true, "WIRE",
                "RISK", 50, null, null, "red");
        questions.add(q5);
        Question q6 = new Question(36,"CORROSION", "Which is the condition of the wire rope after its usage due to corrosion?", "WIRE", true, "WIRE",
                "RISK", 60, null, null, "red");
        questions.add(q6);
        Question q7 = new Question(37,"MECHANICAL DEFORMATIONS (indicative pictures)", "Are kinks / dog legs / bird cages / core protrusion / strand protrusion / twists / waviness formed in the wire mooring line? Any of the below deformations signals an immediate or soon-to-be-effected rope discarding.", "WIRE", true, "WIRE",
                "RISK", 70, null, null, "red");
        questions.add(q7);
        Question q8 = new Question(38,"TERMINATION", "In which condition is your wire rope’s termination?", "WIRE", true, "WIRE",
                "RISK", 80, null, null, "red");
        questions.add(q8);

        /* STORAGE QUESTIONS WIRES */
        Question q9 = new Question(43,"CUTTING & ABRASION", "Which is the condition of wire after its usage due to abrasion and cutting?", "STORAGE", false, "WIRE",
                "EVALUATION", 10, null, null, "green");
        questions.add(q9);
        Question q10 = new Question(44,"BENDING", "In which state is your wire after bending?", "STORAGE", false, "WIRE",
                "EVALUATION", 20, null, null, "green");
        questions.add(q10);
        Question q11 = new Question(45,"CHEMICAL EXPOSURE", "Are there any traces of chemicals on your wires?", "STORAGE", false, "WIRE",
                "EVALUATION", 30, null, null, "green");
        questions.add(q11);
        Question q12 = new Question(46,"STORAGE", "Which is the storage condition of storage of your wire?", "STORAGE", false, "WIRE",
                "RISK", 40, null, null, "blue");
        questions.add(q12);

        return questions;
    }
    public static List<Answer> fillDBWithAnswers() {
        List <Answer> answers = new ArrayList<>();

        /* WIRE QUESTIONS */
        Answer a1_1 = new Answer(1, "Excellent condition", "No signs of wear / abrasion",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 1, 5, "wire_abrasion_5", null);
        answers.add(a1_1);
        Answer a1_2 = new Answer(2, "Good condition", "Minor signs of wear / abrasion",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 1, 4, "wire_abrasion_4", null);
        answers.add(a1_2);
        Answer a1_3 = new Answer(3, "Mediocre condition", "Obvious signs of wear and possible future degradation",
                "CONSULT_MANUFACTURER_GUIDELINES_MEASURE_OUTER_WIRES", 1, 3, "wire_abrasion_3", null);
        answers.add(a1_3);
        Answer a1_4 = new Answer(4, "Poor condition", "Wires have lost metallic area due to abrasion",
                "MEASURE_OUTER_WIRE", 1, 2, "wire_abrasion_2", null);
        answers.add(a1_4);
        Answer a1_5 = new Answer(5, "Extremely poor condition", "Wires have lost substantial metallic area due to abrasion & fractures",
                "RETIRE_OR_IMMEDIATE_TEST_OUTER_WIRE", 1, 1, "wire_abrasion_1", null);
        answers.add(a1_5);

        Answer a2_1 = new Answer(6, "Excellent condition", "No broken wires",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 2, 5, "wire_crown_5", null);
        answers.add(a2_1);
        Answer a2_2 = new Answer(7, "Good condition", "1 broken wire in 6d length / 2 broken wires in 30d length occurring randomly.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 2, 4, "wire_crown_4", null);
        answers.add(a2_2);
        Answer a2_3 = new Answer(8, "Mediocre condition", "2 broken wires in 6d length / 4 broken wires in 30d length occurring randomly.",
                "CONSULT_MANUFACTURER_GUIDELINES", 2, 3, "wire_crown_3", null);
        answers.add(a2_3);
        Answer a2_4 = new Answer(9, "Poor condition ", "3 broken wires in 6d length / 6 broken wires in 30d length occurring randomly.",
                "MAYBE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 2, 2, "wire_crown_2", null);
        answers.add(a2_4);
        Answer a2_5 = new Answer(10, "Extremely poor condition", "4 or more broken wires in 6d length / 8 or more broken wires in 30d length occurring randomly.",
                "RETIRE_OR_IMMEDIATE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 2, 1, "wire_crown_1", null);
        answers.add(a2_5);

        Answer a3_1 = new Answer(11, "Excellent condition", "No valley broken wires. No broken wires at termination.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 3, 5, "wire_valley_5", null);
        answers.add(a3_1);
        Answer a3_2 = new Answer(12, "Good condition", "1 valley broken wire in 6d length. No broken wires at termination.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 3, 4, "wire_valley_4", null);
        answers.add(a3_2);
        Answer a3_3 = new Answer(13, "Mediocre condition", "1 valley broken wire in 6d length or 1 broken wire at termination.",
                "CONSULT_MANUFACTURER_GUIDELINES", 3, 3, "wire_valley_3", null);
        answers.add(a3_3);
        Answer a3_4 = new Answer(14, "Poor condition ", "1 valley broken wire in 6d length AND 1 broken wire at termination.",
                "MAYBE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 3, 2, "wire_valley_2", null);
        answers.add(a3_4);
        Answer a3_5 = new Answer(15, "Extremely poor condition", "2 valley broken wires in 6d length or 2 broken wires at termination (at same or different strand) or if a whole strand is fractured.",
                "RETIRE_OR_IMMEDIATE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 3, 1, "wire_valley_1", null);
        answers.add(a3_5);

        Answer a4_1 = new Answer(16, "Excellent condition", "Diameter decrease 0% - 3,5%",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 4, 5, "wire_diameter_5", null);
        answers.add(a4_1);
        Answer a4_2 = new Answer(17, "Good condition", "Diameter decrease 3,5% – 4,5%.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 4, 4, "wire_diameter_5", null);
        answers.add(a4_2);
        Answer a4_3 = new Answer(18, "Mediocre condition", "Diameter decrease 4,5% – 6,5%.",
                "CONSULT_MANUFACTURER_GUIDELINES", 4, 3, "wire_diameter_5", null);
        answers.add(a4_3);
        Answer a4_4 = new Answer(19, "Poor condition ", "Diameter decrease 6,5% – 10%.",
                "MAYBE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 4, 2, "wire_diameter_5", null);
        answers.add(a4_4);
        Answer a4_5 = new Answer(20, "Extremely poor condition", "Diameter decrease over 10%.",
                "RETIRE_OR_TURN_END_TO_END_ROPE", 4, 1, "wire_diameter_1", null);
        answers.add(a4_5);

        Answer a5_1 = new Answer(21, "Excellent condition", "Diameter increase 0 – 1%.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 5, 5, "wire_diameter_inc_5", null);
        answers.add(a5_1);
        Answer a5_2 = new Answer(22, "Good condition", "Diameter increase 1% – 2%.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 5, 4, "wire_diameter_inc_4", null);
        answers.add(a5_2);
        Answer a5_3 = new Answer(23, "Mediocre condition", "Diameter increase 2% – 3%.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES_BEGIN_INVESTIGATION", 5, 3, "wire_diameter_inc_3", null);
        answers.add(a5_3);
        Answer a5_4 = new Answer(24, "Poor condition ", "Diameter increase 3% – 5%.",
                "PENDING_INVESTIGATION_POSSIBLE_SWELLING_CONSULT_MANUFACTURER_GUIDELINES", 5, 2, "wire_diameter_inc_2", null);
        answers.add(a5_4);
        Answer a5_5 = new Answer(25, "Extremely poor condition", "Diameter increase over 5%.",
                "RETIRE_OR_TURN_END_TO_END_ROPE", 5, 1, "wire_diameter_inc_1", null);
        answers.add(a5_5);

        Answer a6_1 = new Answer(26, "Excellent condition", "No corrosion.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 6, 5, "wire_corrosion_5", null);
        answers.add(a6_1);
        Answer a6_2 = new Answer(27, "Good condition", "Beginning of surface oxidation, can be wiped clean, superficial.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 6, 4, "wire_corrosion_4", null);
        answers.add(a6_2);
        Answer a6_3 = new Answer(28, "Mediocre condition", "Wires rough to touch, general surface oxidation.",
                "MUST_TEST_CONSULT_MANUFACTURER_GUIDELINES", 6, 3, "wire_corrosion_3", null);
        answers.add(a6_3);
        Answer a6_4 = new Answer(29, "Poor condition ", "Surface of wire now greatly affected by oxidation.",
                "MUST_TEST_CONSULT_MANUFACTURER_GUIDELINES", 6, 2, "wire_corrosion_2", null);
        answers.add(a6_4);
        Answer a6_5 = new Answer(30, "Extremely poor condition", "Surface heavily pitted and wires quite slack, gaps between wires",
                "RETIRE_IMMEDIATELY", 6, 1, "wire_corrosion_1", null);
        answers.add(a6_5);

        Answer a7_1 = new Answer(31, "Excellent condition ", "",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 7, 5, "wire_deform_5", null);
        answers.add(a7_1);
        Answer a7_2 = new Answer(32, "Poor condition ", "Kink.",
                "CONSULT_MANUFACTURER_GUIDELINES", 7, 2, "wire_deform_2", null);
        answers.add(a7_2);
        Answer a7_3 = new Answer(33, "Extremely poor & dangerous condition", "Surface heavily pitted and wires quite slack, gaps between wires",
                "RETIRE_IMMEDIATELY", 7, 1, "wire_deform_1", null);
        answers.add(a7_3);

        Answer a8_1 = new Answer(34, "Excellent condition", "No cracks, pitting or corrosion.",
                "CONTINUE_USAGE_SAFE_PRACTICE", 8, 5, "wire_termination_5", null);
        answers.add(a8_1);
        Answer a8_2 = new Answer(35, "Good condition", "External corrosion. No cracks.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 8, 4, "wire_termination_4", null);
        answers.add(a8_2);
        Answer a8_3 = new Answer(36, "Mediocre condition", "Pitting corrosion. No cracks.",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 8, 3, "wire_termination_3", null);
        answers.add(a8_3);
        Answer a8_4 = new Answer(37, "Poor condition ", "Reduction of diameter 3 - 5%.",
                "CONSULT_MANUFACTURER_GUIDELINES", 8, 2, "wire_termination_2", null);
        answers.add(a8_4);
        Answer a8_5 = new Answer(38, "Extremely poor condition", "Diameter of ferrule is less than 95% of the as new condition.",
                "RETIRE_OR_TURN_END_TO_END_ROPE", 8, 1, "wire_termination_1", null);
        answers.add(a8_5);

        Answer a9_1 = new Answer(39, "Excellent condition", "No signs of wear",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 9, 5, "sb_abrasion_5", null);
        answers.add(a9_1);
        Answer a9_2 = new Answer(40, "Good condition", "Minor signs of wear or cut. Less 3% loss of fiber cross-section section in whole core of the rope",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 9, 4, "sb_abrasion_4", null);
        answers.add(a9_2);
        Answer a9_3 = new Answer(41, "Mediocre condition", "Signs of minor external/internal wear or cut. 3% to 5% loss of fiber cross-section section in whole core of the rope",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 9, 3, "sb_cuttong_3", null);
        answers.add(a9_3);
        Answer a9_4 = new Answer(42, "Poor condition", "Signs of medium external/internal wear or cut. Over 5% loss of fiber cross-section section in whole core of the rope",
                "MUST_TEST_CONSULT_MANUFACTURER_GUIDELINES", 9, 2, "sb_cuttong_2", null);
        answers.add(a9_4);
        Answer a9_5 = new Answer(43, "Extremely poor condition", "Signs of major external/internal wear or cut. Over 10% loss of fiber cross-section section in whole core of the rope or in a strand of core",
                "RETIRE_OR_IMMEDIATE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 9, 1, "sb_cuttong_1", null);
        answers.add(a9_5);

        Answer a10_1 = new Answer(44, "Excellent condition", null,
                "CONTINUE_USAGE_SAFE_PRACTICE", 10, 5, "sb_abrasion_5", null);
        answers.add(a10_1);
        Answer a10_2 = new Answer(45, "Good condition", null,
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 10, 4, "bending_4", null);
        answers.add(a10_2);
        Answer a10_3 = new Answer(46, "Mediocre condition", null,
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 10, 3, "bending_3", null);
        answers.add(a10_3);
        Answer a10_4 = new Answer(47, "Poor condition", null,
                "CONSULT_MANUFACTURER_GUIDELINES", 10, 2, "bending_2", null);
        answers.add(a10_4);
        Answer a10_5 = new Answer(48, "Extremely poor condition", null,
                "CONSULT_MANUFACTURER_GUIDELINES", 10, 1, "bending_1", null);
        answers.add(a10_5);

        Answer a11_1 = new Answer(49, "Excellent condition", "No discoloration",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 11, 5, "chemical_5", null);
        answers.add(a11_1);
        Answer a11_2 = new Answer(50, "Good condition", "Minor exposure to grease material",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 11, 4, "chemical_4", null);
        answers.add(a11_2);
        Answer a11_3 = new Answer(51, "Mediocre condition", "Higher exposure to grease material",
                "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", 11, 3, "chemical_3", null);
        answers.add(a11_3);
        Answer a11_4 = new Answer(52, "Poor condition", "High exposure to Chemicals",
                "MAYBE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 11, 2, "chemical_2", null);
        answers.add(a11_4);
        Answer a11_5 = new Answer(53, "Extremely poor condition", "High exposure to chemicals in high concentration",
                "RETIRE_OR_IMMEDIATE_TEST_CONSULT_MANUFACTURER_GUIDELINES", 11, 1, "chemical_1", null);
        answers.add(a11_5);

        Answer a12_1 = new Answer(54, "Safe", "Storage with normal conditions temperature without moisture. Covered storage space. Procedures are in accordance with user’s manual",
                null, 12, 5, null, null);
        answers.add(a12_1);
        Answer a12_2 = new Answer(55, "Low", "Covered storage space. Procedures are in accordance with user’s manual",
                null, 12, 4, null, null);
        answers.add(a12_2);
        Answer a12_3 = new Answer(56, "Medium", "Procedures are in accordance with user’s manual",
                null, 12, 3, null, null);
        answers.add(a12_3);
        Answer a12_4 = new Answer(57, "High ", "Open storage space",
                null, 12, 2, null, null);
        answers.add(a12_4);
        Answer a12_5 = new Answer(58, "Very high", "Open-storage space with un-normal conditions temperature and moisture",
                null, 12, 1, null, null);
        answers.add(a12_5);

        return answers;
    }
    public static List<OutcomeCodes> fillOutcomeCodeTable() {
        List<OutcomeCodes> outcomeCodes = new ArrayList<>();
        OutcomeCodes oc1 = new OutcomeCodes(1, 10, "CONTINUE_USAGE_SAFE_PRACTICE", "Continue usage under the same safe practice", "OVERRIDABLE");
        outcomeCodes.add(oc1);
        OutcomeCodes oc2 = new OutcomeCodes(2, 10, "CONTINUE_USAGE_SAFE_PRACTICE", "Continue the safe to use", "OVERRIDABLE");
        outcomeCodes.add(oc2);
        OutcomeCodes oc3 = new OutcomeCodes(3, 15, "PROCEDURES_IN_ACCORDANCE_WITH_USER_MANUAL", "Procedures are in accordance with user’s manual", "OVERRIDABLE");
        outcomeCodes.add(oc3);
        OutcomeCodes oc4 = new OutcomeCodes(4, 20, "NORMAL_USE", "Normal use", "OVERRIDABLE");
        outcomeCodes.add(oc4);
        OutcomeCodes oc5 = new OutcomeCodes(5, 25, "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", "Continue usage under manufacturer’s guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc5);
        OutcomeCodes oc6 = new OutcomeCodes(6, 30, "CONSULT_MANUFACTURER_GUIDELINES", "Consult the manufacturer's guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc6);
        OutcomeCodes oc7 = new OutcomeCodes(7, 30, "CONSULT_MANUFACTURER_GUIDELINES_MAYBE_JACKET_REPAIR", "Consult manufacturer's guidelines. Maybe you will need to repair the jacket", "OVERRIDABLE");
        outcomeCodes.add(oc7);
        OutcomeCodes oc8 = new OutcomeCodes(8, 40, "CHANGE_PROTECTOR_CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES", "Change your protector with new, with correct material and with correct type. Continue usage under manufacturer’s guidelines", "SHOW");
        outcomeCodes.add(oc8);
        OutcomeCodes oc9 = new OutcomeCodes(9, 40, "REPAIR_JACKET_ACCORDING_TO_MANUFACTURER_GUIDELINES", "Recommended repair actions of the jacket to be taken according to manufacturer’s guidelines", "SHOW\n");
        outcomeCodes.add(oc9);
        OutcomeCodes oc10 = new OutcomeCodes(10, 45, "USE_WITH_EXTREME_CAUTION_CONSULT_MANUFACTURER_GUIDELINES", "Use with extreme caution. Consult manufacturer's guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc10);
        OutcomeCodes oc11 = new OutcomeCodes(11, 50, "IMMEDIATELY_CONSULT_MANUFACTURER_GUIDELINES", "Immediately consult manufacturer's guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc11);
        OutcomeCodes oc12 = new OutcomeCodes(12, 55, "MAYBE_TEST_CONSULT_MANUFACTURER_GUIDELINES", "Maybe you will need test. OCIMF recommends retirement of the rope if the residual strength testing of our rope is below 75%. Consult manufacturer's guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc12);
        OutcomeCodes oc13 = new OutcomeCodes(13, 60, "MUST_TEST_CONSULT_MANUFACTURER_GUIDELINES", "You will need test. OCIMF recommends retirement of the rope if the residual strength testing of our rope is below 75%. Consult manufacturer's guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc13);
        OutcomeCodes oc14 = new OutcomeCodes(14, 70, "DANGEROUS_USE_MUST_TEST_CONSULT_MANUFACTURER_GUIDELINES", "Dangerous use. You will need test. OCIMF recommends retirement of the rope if the residual strength testing of our rope is below 75%", "OVERRIDABLE");
        outcomeCodes.add(oc14);
        OutcomeCodes oc15 = new OutcomeCodes(15, 80, "IMMEDIATELY_TEST_CONSULT_MANUFACTURER_GUIDELINES", "Immediately test. OCIMF recommends retirement of the rope if the residual strength testing of our rope is below 75%. Consult manufacturer's guidelines", "OVERRIDABLE");
        outcomeCodes.add(oc15);
        OutcomeCodes oc16 = new OutcomeCodes(16, 90, "VERY_DANGEROUS_USE_IMMEDIATELY_TEST_CONSULT_MANUFACTURER_GUIDELINES", "Very dangerous use. Immediately test. OCIMF recommends retirement of the rope if the residual strength testing of our rope is below 75%", "OVERRIDABLE");
        outcomeCodes.add(oc16);
        OutcomeCodes oc17 = new OutcomeCodes(17, 100, "RETIRE_OR_IMMEDIATE_TEST_CONSULT_MANUFACTURER_GUIDELINES", "Retirement or immediately test. OCIMF recommends retirement of the rope if the residual strength testing of our rope is below 75%. Consult manufacturer's guidelines", "FINAL");
        outcomeCodes.add(oc17);
        OutcomeCodes oc18 = new OutcomeCodes(18, 65, "MEASURE_OUTER_WIRE", "Measure the outer wires to assess their condition.", "SHOW");
        outcomeCodes.add(oc18);
        OutcomeCodes oc19 = new OutcomeCodes(19, 100, "RETIRE_OR_IMMEDIATE_TEST_OUTER_WIRE", "Retirement or immediately test the outer wires. If outer wires have abraded more than 7%, the rope must be discarded immediately.", "FINAL");
        outcomeCodes.add(oc19);
        OutcomeCodes oc20 = new OutcomeCodes(20, 40, "CONSULT_MANUFACTURER_GUIDELINES_MEASURE_OUTER_WIRES", "Continue usage under manufacturer’s guidelines and measure the outer wires.", "OVERRIDABLE");
        outcomeCodes.add(oc20);
        OutcomeCodes oc21 = new OutcomeCodes(21, 100, "RETIRE_OR_TURN_END_TO_END_ROPE", "Rope must be discarded immediately or turned end-for-end if the remaining length has not been used. Discarded length to be marked as unsuitable.", "FINAL");
        outcomeCodes.add(oc21);
        OutcomeCodes oc22 = new OutcomeCodes(22, 55, "CONTINUE_USAGE_UNDER_MANUFACTURER_GUIDELINES_BEGIN_INVESTIGATION", "Begin investigation of the possible causes that might have caused the swelling. Continue usage under manufacturer’s guidelines.", "OVERRIDABLE");
        outcomeCodes.add(oc22);
        OutcomeCodes oc23 = new OutcomeCodes(23, 70, "PENDING_INVESTIGATION_POSSIBLE_SWELLING_CONSULT_MANUFACTURER_GUIDELINES", "Pending the investigation of the possible causes that might have caused the swelling, continue usage under manufacturer’s guidelines.", "OVERRIDABLE");
        outcomeCodes.add(oc23);
        OutcomeCodes oc24 = new OutcomeCodes(24, 100, "RETIRE_IMMEDIATELY", "Discard immediately.", "FINAL");
        outcomeCodes.add(oc24);

        return outcomeCodes;
    }
}
