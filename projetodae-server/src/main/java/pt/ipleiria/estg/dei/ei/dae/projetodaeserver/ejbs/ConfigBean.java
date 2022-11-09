package pt.ipleiria.estg.dei.ei.dae.projetodaeserver.ejbs;

import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.entities.BiomedicDataType;
import pt.ipleiria.estg.dei.ei.dae.projetodaeserver.utils.MyDateTime;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.time.LocalDateTime;

@Singleton
@Startup
public class ConfigBean {
    @EJB
    PatientBean patientBean;

    @EJB
    MedicBean medicBean;

    @EJB
    AdministratorBean administratorBean;

    @EJB
    BiomedicDataTypeBean biomedicDataTypeBean;

    @EJB
    ObservationBean observationBean;

    @EJB
    PrescriptionBean prescriptionBean;

    @PostConstruct
    public void populateDB(){
        System.out.println("Populating DB...");

        try{

            //Creating Medic
            medicBean.create("Joana_Silva", "Joana Silva", "1234", "924112587", "Bairro Alto", "jsilvamedic@mail.pt");
            medicBean.create("FAndre", "Francisco André", "1234", "917895220", "Rua do Padre", "fandremedic@mail.pt");
            medicBean.create("carlos_ivo", "Carlos Ivo", "1234", "916229372", "Bairro Alto", "jsilvamedic@mail.pt");
            medicBean.create("valerio_rubina", "Valerio Rubina ", "1234", "919182732", "Rua do Padre", "fandremedic@mail.pt");


            patientBean.create("aLoureiro", "Alexandre Loureiro", "1234", "917765678", "Rua da Fé", "alexlou@mail.pt");
            patientBean.create("manel_", "Manuel Teixeira", "1234", "963357810", "Avenia da Luz", "manel@mail.pt");
            patientBean.create("maria_J", "Maria João", "1234", "962344810", "Rua das poças", "mariaJ@mail.pt");
            patientBean.create("magda_vilma", "Magda Vilma", "1234", "963353460", "Avenia Principal", "magda_vilma@mail.pt");
            patientBean.create("adriano_lopes", "Adriano Lopes", "1234", "964316340", "Rua Gil Afonses", "adriano_lopes@mail.pt");
            patientBean.create("filipe_antunes", "Filipe Antunes", "1234", "963416310", "Avenida da Liberdade", "filipe_antunes@mail.pt");
            patientBean.create("ana_fonseca", "Ana Fonseca", "1234", "966431640", "Rua Don Afonso", "ana_fonseca@mail.pt");
            patientBean.create("vitor_bastos", "Vitor Bastos", "1234", "964366310", "Rua das Quintas Altas", "vitor_bastos@mail.pt");
            patientBean.create("rafael_amado", "Rafael Amado", "1234", "963314536", "Avenia Primeira", "rafael_amado@mail.pt");
            patientBean.create("joao_cardoso", "Joao Cardoso", "1234", "962345810", "Rua das Silvas Amadas", "joao_cardoso@mail.pt");
            patientBean.create("miriam_coelho", "Miriam Coelho", "1234", "913435810", "Beco do Alvarante", "miriam_coelho@mail.pt");
            patientBean.create("jose_esteves", "José Esteves", "1234", "915355350", "Avenida das Três Árvores", "jose_esteves@mail.pt");
            patientBean.create("andre_ferreira", "André Ferreira", "1234", "913234510", "Rua da Palestina", "andre_ferreira@mail.pt");
            patientBean.create("elsa_lavos", "Elsa Lavos", "1234", "914324210", "Avenia do Bairro Cabaz", "elsa_lavos@mail.pt");
            patientBean.create("artur_lopes", "Artur Lopes", "1234", "962342342", "Rua silvestre das Armadas", "artur_lopes@mail.pt");

            medicBean.addPatientToMedic("Joana_Silva","aLoureiro");
            medicBean.addPatientToMedic("FAndre","manel_");
            medicBean.addPatientToMedic("Joana_Silva","maria_J");
            medicBean.addPatientToMedic("carlos_ivo","magda_vilma");
            medicBean.addPatientToMedic("valerio_rubina","adriano_lopes");
            medicBean.addPatientToMedic("FAndre","filipe_antunes");
            medicBean.addPatientToMedic("carlos_ivo","ana_fonseca");
            medicBean.addPatientToMedic("valerio_rubina","vitor_bastos");
            medicBean.addPatientToMedic("Joana_Silva","rafael_amado");
            medicBean.addPatientToMedic("FAndre","joao_cardoso");
            medicBean.addPatientToMedic("valerio_rubina","miriam_coelho");
            medicBean.addPatientToMedic("Joana_Silva","jose_esteves");
            medicBean.addPatientToMedic("FAndre","andre_ferreira");
            medicBean.addPatientToMedic("valerio_rubina","elsa_lavos");
            medicBean.addPatientToMedic("Joana_Silva","artur_lopes");

            //Creating Administrator
            administratorBean.create("fjss", "Filipe Sousa", "1234", "917822979", "Rua do Almirante", "fjss@mail.pt");
            administratorBean.create("dbatista", "David Batista", "1234", "925578612", "Rua Santiago", "dbatista@mail.pt");
            administratorBean.create("fcarvalho", "Frederico Carvalho", "1234", "963351679", "Rua da Fonte", "fcarvalho@mail.pt");

            biomedicDataTypeBean.create("Heart Rate","Measure the patients heart rate in beats per minute",40,250,"BPM");
            biomedicDataTypeBean.create("Blood Oxigen Level","Measure the patients oxigen in the blood",80,100,"%");
            biomedicDataTypeBean.create("Systolic  Blood Pressure","Measures the pressure in your arteries when your heart beats",60,240,"mmHg");
            biomedicDataTypeBean.create("Diastolic Blood Pressure","Measures the pressure in your arteries when your heart rests between beats",30,200,"mmHg");
            biomedicDataTypeBean.create("Mercury","Measures the ammount of mercury found in the patients blood",0,1,"mg/L");
            biomedicDataTypeBean.create("Total Cholesterol","This value indicates the total amount of cholesterol in the blood",0,300,"mg/dL");
            biomedicDataTypeBean.create("HDL Cholesterol", "It's known as the \"good\" type of cholesterol, as it is linked to a protein that transports it from the blood to the liver, where it is eliminated in the feces, if it is in excess",0,100,"mg/dL");
            biomedicDataTypeBean.create("LDL Cholesterol", "This is the popular \"bad\" cholesterol, which is linked to a protein that transports it from the liver to cells and veins, where it accumulates and can cause cardiovascular problems",0,200,"mg/dL");

            observationBean.create("aLoureiro","Joana_Silva", MyDateTime.now(),1,"Bad",60);
            observationBean.create("aLoureiro","Joana_Silva", new MyDateTime("17:23:37.26/12/2021"),1,"Good",112);
            observationBean.create("aLoureiro","Joana_Silva", new MyDateTime(false,29,12,2021),1,"Good",112);


            prescriptionBean.create("aLoureiro", new MyDateTime("17:45:00.27/12/2021"), new MyDateTime("8:30:00.01/01/2022"), "Dieta", "Ingerir alimentos com poderes terapêuticos", 1);
            prescriptionBean.create("aLoureiro", new MyDateTime("09:30:33.01/01/2022"), new MyDateTime("8:30:00.10/05/2022"), "Atividade Física", "Fazer, pelo menos, 20 minutos de corrida por dia", 2);
            prescriptionBean.create("aLoureiro", new MyDateTime(false,29,12,2021), MyDateTime.now(), "Dieta", "Fazer abdominais", 2);
            prescriptionBean.create("aLoureiro", MyDateTime.now(), new MyDateTime(false,29,12,2022), "Exercico", "Correr 15min todos os dias", 2);

        } catch (Exception e){

        }
    }
}
