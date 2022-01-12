package com.mediscreen.riskAssessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mediscreen.commons.constants.RiskLevel;
import com.mediscreen.commons.dto.NoteDTO;
import com.mediscreen.commons.dto.PatientDTO;
import com.mediscreen.commons.dto.RiskAssessmentDTO;
import com.mediscreen.riskAssessment.constants.TestConstants;
import com.mediscreen.riskAssessment.service.contracts.IRiskAssessmentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiabetesRiskAssessmentServiceTest {

    private static PatientDTO maleAboveRiskAgeLimit;
    private static PatientDTO maleUnderRiskAgeLimit;
    private static PatientDTO maleAtRiskAgeLimit;
    private static PatientDTO femaleAboveRiskAgeLimit;
    private static PatientDTO femaleUnderRiskAgeLimit;
    private static PatientDTO femaleAtRiskAgeLimit;

    private static List<NoteDTO> noteListWithNoRiskFactor;
    private static List<NoteDTO> noteListWithOneRiskFactor;
    private static List<NoteDTO> noteListWithTwoRiskFactors;
    private static List<NoteDTO> noteListWithThreeRiskFactors;
    private static List<NoteDTO> noteListWithFourRiskFactors;
    private static List<NoteDTO> noteListWithFiveRiskFactors;
    private static List<NoteDTO> noteListWithSixRiskFactors;
    private static List<NoteDTO> noteListWithSevenRiskFactors;
    private static List<NoteDTO> noteListWithEightRiskFactors;
    private static List<NoteDTO> noteListWithTenRiskFactors;


    @Autowired
    IRiskAssessmentService riskAssessmentService;

    @BeforeAll
    static void setUp() {

        /* patients above risk age limit */
        maleAboveRiskAgeLimit = new PatientDTO();
        maleAboveRiskAgeLimit.setId(TestConstants.PATIENT1_ID);
        maleAboveRiskAgeLimit.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        maleAboveRiskAgeLimit.setLastname(TestConstants.PATIENT1_LASTNAME);
        maleAboveRiskAgeLimit.setBirthDate(TestConstants.BIRTHDATE_ABOVE_THIRTY);
        maleAboveRiskAgeLimit.setGender(TestConstants.GENDER_MALE);
        maleAboveRiskAgeLimit.setAddress(TestConstants.PATIENT1_ADDRESS);
        maleAboveRiskAgeLimit.setPhone(TestConstants.PATIENT1_PHONE);

        femaleAboveRiskAgeLimit = new PatientDTO();
        femaleAboveRiskAgeLimit.setId(TestConstants.PATIENT1_ID);
        femaleAboveRiskAgeLimit.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        femaleAboveRiskAgeLimit.setLastname(TestConstants.PATIENT1_LASTNAME);
        femaleAboveRiskAgeLimit.setBirthDate(TestConstants.BIRTHDATE_ABOVE_THIRTY);
        femaleAboveRiskAgeLimit.setGender(TestConstants.GENDER_FEMALE);
        femaleAboveRiskAgeLimit.setAddress(TestConstants.PATIENT1_ADDRESS);
        femaleAboveRiskAgeLimit.setPhone(TestConstants.PATIENT1_PHONE);

        /* patients under risk age limit */
        maleUnderRiskAgeLimit = new PatientDTO();
        maleUnderRiskAgeLimit.setId(TestConstants.PATIENT1_ID);
        maleUnderRiskAgeLimit.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        maleUnderRiskAgeLimit.setLastname(TestConstants.PATIENT1_LASTNAME);
        maleUnderRiskAgeLimit.setBirthDate(TestConstants.BIRTHDATE_UNDER_THIRTY);
        maleUnderRiskAgeLimit.setGender(TestConstants.GENDER_MALE);
        maleUnderRiskAgeLimit.setAddress(TestConstants.PATIENT1_ADDRESS);
        maleUnderRiskAgeLimit.setPhone(TestConstants.PATIENT1_PHONE);

        femaleUnderRiskAgeLimit = new PatientDTO();
        femaleUnderRiskAgeLimit.setId(TestConstants.PATIENT1_ID);
        femaleUnderRiskAgeLimit.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        femaleUnderRiskAgeLimit.setLastname(TestConstants.PATIENT1_LASTNAME);
        femaleUnderRiskAgeLimit.setBirthDate(TestConstants.BIRTHDATE_UNDER_THIRTY);
        femaleUnderRiskAgeLimit.setGender(TestConstants.GENDER_FEMALE);
        femaleUnderRiskAgeLimit.setAddress(TestConstants.PATIENT1_ADDRESS);
        femaleUnderRiskAgeLimit.setPhone(TestConstants.PATIENT1_PHONE);

        /* patients at risk age limit */
        maleAtRiskAgeLimit = new PatientDTO();
        maleAtRiskAgeLimit.setId(TestConstants.PATIENT1_ID);
        maleAtRiskAgeLimit.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        maleAtRiskAgeLimit.setLastname(TestConstants.PATIENT1_LASTNAME);
        maleAtRiskAgeLimit.setBirthDate(TestConstants.BIRTHDATE_THIRTY);
        maleAtRiskAgeLimit.setGender(TestConstants.GENDER_MALE);
        maleAtRiskAgeLimit.setAddress(TestConstants.PATIENT1_ADDRESS);
        maleAtRiskAgeLimit.setPhone(TestConstants.PATIENT1_PHONE);

        femaleAtRiskAgeLimit = new PatientDTO();
        femaleAtRiskAgeLimit.setId(TestConstants.PATIENT1_ID);
        femaleAtRiskAgeLimit.setFirstname(TestConstants.PATIENT1_FIRSTNAME);
        femaleAtRiskAgeLimit.setLastname(TestConstants.PATIENT1_LASTNAME);
        femaleAtRiskAgeLimit.setBirthDate(TestConstants.BIRTHDATE_THIRTY);
        femaleAtRiskAgeLimit.setGender(TestConstants.GENDER_FEMALE);
        femaleAtRiskAgeLimit.setAddress(TestConstants.PATIENT1_ADDRESS);
        femaleAtRiskAgeLimit.setPhone(TestConstants.PATIENT1_PHONE);

        /* notes */
        NoteDTO noteWithNoRiskFactor = new NoteDTO(TestConstants.NOTE_ID1, TestConstants.NOTE_PATIENT_ID,
                                                   TestConstants.NOTES_WITHOUT_RISK_FACTOR,
                                                   TestConstants.NOTE_CREATION_DATE,
                                                   TestConstants.NOTE_LAST_UPDATE_DATE);
        NoteDTO noteWithOneRiskFactor = new NoteDTO(TestConstants.NOTE_ID2, TestConstants.NOTE_PATIENT_ID,
                                                    TestConstants.NOTES_WITH_ONE_RISK_FACTOR,
                                                    TestConstants.NOTE_CREATION_DATE,
                                                    TestConstants.NOTE_LAST_UPDATE_DATE);
        NoteDTO noteWithTwoRiskFactors = new NoteDTO(TestConstants.NOTE_ID3, TestConstants.NOTE_PATIENT_ID,
                                                     TestConstants.NOTES_WITH_TWO_RISK_FACTOR,
                                                     TestConstants.NOTE_CREATION_DATE,
                                                     TestConstants.NOTE_LAST_UPDATE_DATE);

        /* list of notes */
        noteListWithNoRiskFactor = new ArrayList<>();
        noteListWithNoRiskFactor.add(noteWithNoRiskFactor);

        noteListWithOneRiskFactor = new ArrayList<>();
        noteListWithOneRiskFactor.add(noteWithOneRiskFactor);

        noteListWithTwoRiskFactors = new ArrayList<>();
        noteListWithTwoRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithThreeRiskFactors = new ArrayList<>();
        noteListWithThreeRiskFactors.add(noteWithOneRiskFactor);
        noteListWithThreeRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithFourRiskFactors = new ArrayList<>();
        noteListWithFourRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithFourRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithFiveRiskFactors = new ArrayList<>();
        noteListWithFiveRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithFiveRiskFactors.add(noteWithOneRiskFactor);
        noteListWithFiveRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithSixRiskFactors = new ArrayList<>();
        noteListWithSixRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithSixRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithSixRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithSevenRiskFactors = new ArrayList<>();
        noteListWithSevenRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithSevenRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithSevenRiskFactors.add(noteWithOneRiskFactor);
        noteListWithSevenRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithEightRiskFactors = new ArrayList<>();
        noteListWithEightRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithEightRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithEightRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithEightRiskFactors.add(noteWithTwoRiskFactors);

        noteListWithTenRiskFactors = new ArrayList<>();
        noteListWithTenRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithTenRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithTenRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithTenRiskFactors.add(noteWithTwoRiskFactors);
        noteListWithTenRiskFactors.add(noteWithTwoRiskFactors);
    }

    public static Stream<Arguments> provideArgsForEvaluateRisk() {

        return Stream.of(
            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithNoRiskFactor), RiskLevel.NONE),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithNoRiskFactor), RiskLevel.NONE),
            Arguments.of(new RiskAssessmentDTO(maleAtRiskAgeLimit, noteListWithNoRiskFactor), RiskLevel.NONE),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithNoRiskFactor), RiskLevel.NONE),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithNoRiskFactor), RiskLevel.NONE),
            Arguments.of(new RiskAssessmentDTO(femaleAtRiskAgeLimit, noteListWithNoRiskFactor), RiskLevel.NONE),

            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithTwoRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithTwoRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithThreeRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithThreeRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithFourRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithFourRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithFiveRiskFactors),
                         RiskLevel.BORDERLINE),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithFiveRiskFactors),
                         RiskLevel.BORDERLINE),

            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithSixRiskFactors), RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithSixRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithSevenRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithSevenRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithThreeRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithFourRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithFourRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithFiveRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithSixRiskFactors),
                         RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(maleAtRiskAgeLimit, noteListWithThreeRiskFactors), RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(maleAtRiskAgeLimit, noteListWithFourRiskFactors), RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleAtRiskAgeLimit, noteListWithFourRiskFactors), RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleAtRiskAgeLimit, noteListWithFiveRiskFactors), RiskLevel.IN_DANGER),
            Arguments.of(new RiskAssessmentDTO(femaleAtRiskAgeLimit, noteListWithSixRiskFactors), RiskLevel.IN_DANGER),

            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithEightRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithEightRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithTenRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithTenRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithFiveRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithEightRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithSevenRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithTenRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(maleAtRiskAgeLimit, noteListWithFiveRiskFactors), RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(maleAtRiskAgeLimit, noteListWithTenRiskFactors), RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(femaleAtRiskAgeLimit, noteListWithSevenRiskFactors),
                         RiskLevel.EARLY_ONSET),
            Arguments.of(new RiskAssessmentDTO(femaleAtRiskAgeLimit, noteListWithTenRiskFactors),
                         RiskLevel.EARLY_ONSET),

            Arguments.of(new RiskAssessmentDTO(maleAboveRiskAgeLimit, noteListWithOneRiskFactor),
                         RiskLevel.NOT_APPLICABLE),
            Arguments.of(new RiskAssessmentDTO(femaleAboveRiskAgeLimit, noteListWithOneRiskFactor),
                         RiskLevel.NOT_APPLICABLE),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithOneRiskFactor),
                         RiskLevel.NOT_APPLICABLE),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithOneRiskFactor),
                         RiskLevel.NOT_APPLICABLE),
            Arguments.of(new RiskAssessmentDTO(maleUnderRiskAgeLimit, noteListWithTwoRiskFactors),
                         RiskLevel.NOT_APPLICABLE),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithTwoRiskFactors),
                         RiskLevel.NOT_APPLICABLE),
            Arguments.of(new RiskAssessmentDTO(femaleUnderRiskAgeLimit, noteListWithThreeRiskFactors),
                         RiskLevel.NOT_APPLICABLE)
                        );
    }

    @ParameterizedTest
    @MethodSource("provideArgsForEvaluateRisk")
    void evaluateRisk(RiskAssessmentDTO riskAssessmentDTO, RiskLevel expectedRiskLevel) {
        RiskLevel riskLevel = riskAssessmentService.evaluateRisk(riskAssessmentDTO);
        assertEquals(expectedRiskLevel, riskLevel);
    }
}