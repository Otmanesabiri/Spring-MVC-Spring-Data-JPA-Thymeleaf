package ma.enset.hospitalapp.config;

import ma.enset.hospitalapp.entities.*;
import ma.enset.hospitalapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MedecinRepository medecinRepository;
    
    @Autowired
    private InfirmierRepository infirmierRepository;
    
    @Autowired
    private PersonnelAdministratifRepository personnelAdministratifRepository;
    
    @Autowired
    private TechnicienRepository technicienRepository;
    
    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialiser quelques médecins
        initializeMedecins();
        
        // Initialiser quelques infirmiers
        initializeInfirmiers();
        
        // Initialiser du personnel administratif
        initializePersonnelAdministratif();
        
        // Initialiser des techniciens
        initializeTechniciens();
        
        // Initialiser des rendez-vous
        initializeRendezVous();
        
        // Initialiser quelques rendez-vous
        initializeRendezVous();
        
        // Initialiser des rendez-vous exemples
        initializeRendezVous();
    }

    private void initializeMedecins() throws ParseException {
        if (medecinRepository.count() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            
            Medecin medecin1 = new Medecin();
            medecin1.setNom("Alami");
            medecin1.setPrenom("Hassan");
            medecin1.setEmail("hassan.alami@hospital.ma");
            medecin1.setTelephone("0661234567");
            medecin1.setSpecialite("Cardiologie");
            medecin1.setNumeroOrdre("MD001");
            medecin1.setDateEmbauche(sdf.parse("2020-01-15"));
            medecin1.setHeureDebutTravail(timeFormat.parse("08:00"));
            medecin1.setHeureFinTravail(timeFormat.parse("17:00"));
            medecin1.setActif(true);
            medecin1.setStatut(StatutMedecin.TITULAIRE);
            medecin1.setSalaire(15000.0);
            
            Medecin medecin2 = new Medecin();
            medecin2.setNom("Benali");
            medecin2.setPrenom("Fatima");
            medecin2.setEmail("fatima.benali@hospital.ma");
            medecin2.setTelephone("0662345678");
            medecin2.setSpecialite("Pédiatrie");
            medecin2.setNumeroOrdre("MD002");
            medecin2.setDateEmbauche(sdf.parse("2019-03-20"));
            medecin2.setHeureDebutTravail(timeFormat.parse("09:00"));
            medecin2.setHeureFinTravail(timeFormat.parse("18:00"));
            medecin2.setActif(true);
            medecin2.setStatut(StatutMedecin.TITULAIRE);
            medecin2.setSalaire(14000.0);
            
            Medecin medecin3 = new Medecin();
            medecin3.setNom("Tazi");
            medecin3.setPrenom("Ahmed");
            medecin3.setEmail("ahmed.tazi@hospital.ma");
            medecin3.setTelephone("0663456789");
            medecin3.setSpecialite("Neurologie");
            medecin3.setNumeroOrdre("MD003");
            medecin3.setDateEmbauche(sdf.parse("2021-06-10"));
            medecin3.setHeureDebutTravail(timeFormat.parse("07:30"));
            medecin3.setHeureFinTravail(timeFormat.parse("16:30"));
            medecin3.setActif(true);
            medecin3.setStatut(StatutMedecin.CONTRACTUEL);
            medecin3.setSalaire(13000.0);
            
            medecinRepository.save(medecin1);
            medecinRepository.save(medecin2);
            medecinRepository.save(medecin3);
        }
    }

    private void initializeInfirmiers() throws ParseException {
        if (infirmierRepository.count() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            Infirmier infirmier1 = new Infirmier();
            infirmier1.setNom("Ouali");
            infirmier1.setPrenom("Khadija");
            infirmier1.setEmail("khadija.ouali@hospital.ma");
            infirmier1.setTelephone("0664567890");
            infirmier1.setSpecialite("Soins généraux");
            infirmier1.setDiplome("Diplôme d'État d'Infirmier");
            infirmier1.setDateEmbauche(sdf.parse("2020-09-01"));
            infirmier1.setEquipe(EquipeType.MATIN);
            infirmier1.setHoraire(HoraireTravail.TEMPS_PLEIN);
            infirmier1.setActif(true);
            infirmier1.setStatut(StatutEmploye.ACTIF);
            infirmier1.setSalaire(8000.0);
            infirmier1.setService("Cardiologie");
            
            Infirmier infirmier2 = new Infirmier();
            infirmier2.setNom("Mansouri");
            infirmier2.setPrenom("Youssef");
            infirmier2.setEmail("youssef.mansouri@hospital.ma");
            infirmier2.setTelephone("0665678901");
            infirmier2.setSpecialite("Soins intensifs");
            infirmier2.setDiplome("Diplôme d'État d'Infirmier - Spécialité Réanimation");
            infirmier2.setDateEmbauche(sdf.parse("2019-11-15"));
            infirmier2.setEquipe(EquipeType.NUIT);
            infirmier2.setHoraire(HoraireTravail.TEMPS_PLEIN);
            infirmier2.setActif(true);
            infirmier2.setStatut(StatutEmploye.ACTIF);
            infirmier2.setSalaire(9000.0);
            infirmier2.setService("Réanimation");
            
            infirmierRepository.save(infirmier1);
            infirmierRepository.save(infirmier2);
        }
    }

    private void initializePersonnelAdministratif() throws ParseException {
        if (personnelAdministratifRepository.count() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            PersonnelAdministratif admin1 = new PersonnelAdministratif();
            admin1.setNom("Idrissi");
            admin1.setPrenom("Aicha");
            admin1.setEmail("aicha.idrissi@hospital.ma");
            admin1.setTelephone("0666789012");
            admin1.setPoste("Réceptionniste");
            admin1.setDepartement("Accueil");
            admin1.setDateEmbauche(sdf.parse("2021-02-01"));
            admin1.setHoraire(HoraireTravail.TEMPS_PLEIN);
            admin1.setActif(true);
            admin1.setStatut(StatutEmploye.ACTIF);
            admin1.setSalaire(5000.0);
            admin1.setNiveauAcces(NiveauAcces.BASIQUE);
            
            PersonnelAdministratif admin2 = new PersonnelAdministratif();
            admin2.setNom("Hakimi");
            admin2.setPrenom("Omar");
            admin2.setEmail("omar.hakimi@hospital.ma");
            admin2.setTelephone("0667890123");
            admin2.setPoste("Gestionnaire RH");
            admin2.setDepartement("Ressources Humaines");
            admin2.setDateEmbauche(sdf.parse("2018-05-20"));
            admin2.setHoraire(HoraireTravail.TEMPS_PLEIN);
            admin2.setActif(true);
            admin2.setStatut(StatutEmploye.ACTIF);
            admin2.setSalaire(7000.0);
            admin2.setNiveauAcces(NiveauAcces.AVANCE);
            
            personnelAdministratifRepository.save(admin1);
            personnelAdministratifRepository.save(admin2);
        }
    }

    private void initializeTechniciens() throws ParseException {
        if (technicienRepository.count() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            Technicien technicien1 = new Technicien();
            technicien1.setNom("Chraibi");
            technicien1.setPrenom("Rachid");
            technicien1.setEmail("rachid.chraibi@hospital.ma");
            technicien1.setTelephone("0668901234");
            technicien1.setTypeTechnicien(TypeTechnicien.LABORATOIRE);
            technicien1.setSpecialite("Analyses biologiques");
            technicien1.setCertification("Certificat Technicien de Laboratoire");
            technicien1.setDateEmbauche(sdf.parse("2020-04-10"));
            technicien1.setDateExpirationCertification(sdf.parse("2025-04-10"));
            technicien1.setHoraire(HoraireTravail.TEMPS_PLEIN);
            technicien1.setActif(true);
            technicien1.setStatut(StatutEmploye.ACTIF);
            technicien1.setSalaire(6000.0);
            technicien1.setEquipementsGeres("Automates d'analyses, Microscopes");
            
            Technicien technicien2 = new Technicien();
            technicien2.setNom("Fassi");
            technicien2.setPrenom("Laila");
            technicien2.setEmail("laila.fassi@hospital.ma");
            technicien2.setTelephone("0669012345");
            technicien2.setTypeTechnicien(TypeTechnicien.RADIOLOGIE);
            technicien2.setSpecialite("Imagerie médicale");
            technicien2.setCertification("Certificat Manipulateur en Électroradiologie");
            technicien2.setDateEmbauche(sdf.parse("2019-08-25"));
            technicien2.setDateExpirationCertification(sdf.parse("2024-08-25"));
            technicien2.setHoraire(HoraireTravail.TEMPS_PLEIN);
            technicien2.setActif(true);
            technicien2.setStatut(StatutEmploye.ACTIF);
            technicien2.setSalaire(6500.0);
            technicien2.setEquipementsGeres("Scanner, IRM, Radiographie");
            
            technicienRepository.save(technicien1);
            technicienRepository.save(technicien2);
        }
    }
    
    private void initializeRendezVous() throws ParseException {
        if (rendezVousRepository.count() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            // Récupérer quelques patients et médecins existants
            if (patientRepository.count() > 0 && medecinRepository.count() > 0) {
                Patient patient1 = patientRepository.findAll().get(0);
                Patient patient2 = patientRepository.findAll().size() > 1 ? patientRepository.findAll().get(1) : patient1;
                
                Medecin medecin1 = medecinRepository.findAll().get(0);
                Medecin medecin2 = medecinRepository.findAll().size() > 1 ? medecinRepository.findAll().get(1) : medecin1;
                
                RendezVous rdv1 = new RendezVous();
                rdv1.setDateHeureRendezVous(sdf.parse("2025-01-15 09:00"));
                rdv1.setPatient(patient1);
                rdv1.setMedecin(medecin1);
                rdv1.setStatut(StatutRendezVous.PLANIFIE);
                rdv1.setType(TypeRendezVous.CONSULTATION);
                rdv1.setMotif("Consultation de routine");
                rdv1.setDureeMinutes(30);
                rdv1.setNotes("Premier rendez-vous du patient");
                
                RendezVous rdv2 = new RendezVous();
                rdv2.setDateHeureRendezVous(sdf.parse("2025-01-15 10:30"));
                rdv2.setPatient(patient2);
                rdv2.setMedecin(medecin1);
                rdv2.setStatut(StatutRendezVous.CONFIRME);
                rdv2.setType(TypeRendezVous.CONTROLE);
                rdv2.setMotif("Contrôle post-opératoire");
                rdv2.setDureeMinutes(45);
                rdv2.setNotes("Vérification de la cicatrisation");
                
                RendezVous rdv3 = new RendezVous();
                rdv3.setDateHeureRendezVous(sdf.parse("2025-01-16 14:00"));
                rdv3.setPatient(patient1);
                rdv3.setMedecin(medecin2);
                rdv3.setStatut(StatutRendezVous.PLANIFIE);
                rdv3.setType(TypeRendezVous.EXAMEN);
                rdv3.setMotif("Examen cardiologique");
                rdv3.setDureeMinutes(60);
                rdv3.setNotes("Examen complet avec ECG");
                
                rendezVousRepository.save(rdv1);
                rendezVousRepository.save(rdv2);
                rendezVousRepository.save(rdv3);
            }
        }
    }
}
