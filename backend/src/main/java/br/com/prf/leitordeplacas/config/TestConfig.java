package br.com.prf.leitordeplacas.config;

import br.com.prf.leitordeplacas.model.*;
import br.com.prf.leitordeplacas.model.enums.*;
import br.com.prf.leitordeplacas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CrimeRepository crimeRepository;

    @Autowired
    private CriminalRecordRepository criminalRecordRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScanHistoryRepository scanHistoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        AppUser admin = new AppUser();
        admin.setUsername("admin@prf.gov.br");
        admin.setPassword(passwordEncoder.encode("@Admin_PRF123"));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setEnabled(true);
        admin.setFailedLoginAttempts(0);
        admin.setAccountLocked(false);
        admin.setLockTime(null);
        admin.setConsecutiveLockouts(0);

        AppUser standard = new AppUser();
        standard.setUsername("user@prf.gov.br");
        standard.setPassword(passwordEncoder.encode("@Usuario123"));
        standard.setRole(Role.STANDARD);
        standard.setEnabled(true);
        standard.setEnabled(true);
        standard.setFailedLoginAttempts(0);
        standard.setAccountLocked(false);
        standard.setLockTime(null);
        standard.setConsecutiveLockouts(0);

        appUserRepository.saveAll(Arrays.asList(admin, standard));

        // =================================================================
        // 1. Endereços
        // =================================================================
        Address ad1 = new Address("Rua dos Girassóis", "123", "Casa A", "Meireles", "Fortaleza", "Ceará", "60160-000");
        Address ad2 = new Address("Avenida Bezerra de Menezes", "2045", "AP 801", "Parquelândia", "Fortaleza", "Ceará", "60325-002");
        Address ad3 = new Address("Rua 45", "99", "Casa do muro amarelo", "Jereissati II", "Maracanaú", "Ceará", "61901-050");
        Address ad4 = new Address("Rua Professor Anacleto", "300", "Bloco C, AP 102", "Benfica", "Fortaleza", "Ceará", "60015-270");

        addressRepository.saveAll(Arrays.asList(ad1, ad2, ad3, ad4));

        // =================================================================
        // 2. Pessoas
        // =================================================================
        Person p1 = new Person("João Alves Barbosa Machado", "Masculino", "2001123456789", "111.222.333-44", LocalDate.parse("1995-05-10"), LicenseCategory.AB, ad2);
        Person p2 = new Person("Ana Cecília Medeiros Lima", "Feminino", "2005987654321", "555.666.777-88", LocalDate.parse("2001-02-25"), LicenseCategory.B, ad1);
        Person p3 = new Person("Bruno Aguiar de Melo", "Masculino", "2010112233445", "999.888.777-66", LocalDate.parse("1975-11-30"), LicenseCategory.A, ad3);
        Person p4 = new Person("Letícia Martins Suzuki", "Feminino", "2008556677889", "123.456.789-00", LocalDate.parse("1995-07-18"), LicenseCategory.A, ad1);
        Person p5 = new Person("Fernando de Carvalho Costa Filho", "Masculino", "2012345678901", "444.333.222-11", LocalDate.parse("2004-09-01"), LicenseCategory.C, ad4);
        Person p6 = new Person("Juliana Fernandes Aragão", "Feminino", "2015987654321", "777.666.555-44", LocalDate.parse("1999-04-12"), LicenseCategory.AB, ad2);

        p1.setPhotoFileName("JoaoAlvesBarbosaMachado-Foto3x4.jpeg");
        p2.setPhotoFileName("AnaCeciliaMedeirosLima-Foto3x4.jpeg");
        p3.setPhotoFileName("BrunoAguiardeMelo-Foto3x4.jpeg");
        p4.setPhotoFileName("LeticiaMartinsSuzuki-Foto3x4.jpeg");
        p5.setPhotoFileName(null);
        p6.setPhotoFileName("JulianaFernandesAragao-Foto3x4.jpeg");
        personRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6));

        // =================================================================
        // 3. Crimes
        // =================================================================
        Crime c1 = new Crime("Furto de Veículo", LocalDateTime.parse("2024-05-15T22:30:00"), "Veículo furtado na Av. 13 de Maio.", CrimeStatus.SOB_INVESTIGACAO);
        Crime c2 = new Crime("Tráfico de Drogas", LocalDateTime.parse("2020-07-20T14:00:00"), "Prisão em flagrante com 5kg de entorpecentes.", CrimeStatus.CONDENADO);
        Crime c3 = new Crime("Estelionato", LocalDateTime.parse("2021-02-10T11:00:00"), "Aplicação de golpe de pirâmide financeira.", CrimeStatus.CONDENADO);
        Crime c4 = new Crime("Excesso de Velocidade", LocalDateTime.parse("2025-01-30T08:15:00"), "Radar na Av. Washington Soares - 85km/h.", CrimeStatus.ARQUIVADO);
        Crime c5 = new Crime("Homicídio Qualificado", LocalDateTime.parse("2018-11-05T01:45:00"), "Investigação sobre homicídio no bairro Pirambu.", CrimeStatus.SUSPENSO);
        Crime c6 = new Crime("Agressão", LocalDateTime.parse("2023-09-12T20:00:00"), "Briga em saída de bar.", CrimeStatus.ABSOLVIDO);
        Crime c7 = new Crime("Receptação", LocalDateTime.parse("2022-06-18T16:20:00"), "Prisão com posse de produtos roubados.", CrimeStatus.DENUNCIADO);

        crimeRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7));

        // =================================================================
        // 4. Fichas Criminais
        // =================================================================
        // CASO: Criminoso perigoso com múltiplos crimes e status diferentes
        CriminalRecord cr1 = new CriminalRecord(p1);
        cr1.addCrime(c2); // Condenado por tráfico
        cr1.addCrime(c3); // Condenado por estelionato
        cr1.addCrime(c5); // Suspenso por Homicídio
        cr1.addCrime(c7); // Investigado por Receptação

        // CASO: Pessoa sem antecedentes
        CriminalRecord cr2 = new CriminalRecord(p2);

        // CASO: Pessoa com um crime antigo e arquivado
        CriminalRecord cr3 = new CriminalRecord(p3);
        cr3.addCrime(c4); // Infração de trânsito arquivada

        // CASO: Pessoa sem antecedentes
        CriminalRecord cr4 = new CriminalRecord(p4);

        // CASO: Pessoa com um caso absolvido
        CriminalRecord cr5 = new CriminalRecord(p5);
        cr5.addCrime(c6);

        // CASO: Pessoa que cometeu o furto do veículo (veja v3)
        CriminalRecord cr6 = new CriminalRecord(p6);
        cr6.addCrime(c1);

        criminalRecordRepository.saveAll(Arrays.asList(cr1, cr2, cr3, cr4, cr5, cr6));

        // =================================================================
        // 5. Veículos
        // =================================================================
        // CASO: Veículo de pessoa com ficha criminal extensa
        Vehicle v1 = new Vehicle("BRA49CC", "YAMAHA", "XT 660R", "Azul", 2018, IpvaStatus.PAGO, VehicleType.MOTOCICLETA, p1);

        // CASO: Veículo de pessoa sem antecedentes, tudo OK
        Vehicle v2 = new Vehicle("QRM7E33", "HYUNDAI", "HB20", "Prata", 2022, IpvaStatus.PAGO, VehicleType.CARRO, p2);

        // !! CASO IMPORTANTE: Veículo com queixa de roubo/furto !!
        Vehicle v3 = new Vehicle("BRA2E19", "FIAT", "STRADA", "Vermelha", 2021, IpvaStatus.PAGO, VehicleType.CARRO, p5);

        // CASO: Pessoa sem antecedentes, com veículo de IPVA atrasado
        Vehicle v4 = new Vehicle("DHM6197", "HONDA", "BIZ 125", "Branca", 2020, IpvaStatus.ATRASADO, VehicleType.MOTOCICLETA, p4);

        // CASO: Veículo antigo, isento de IPVA, dono com crime leve
        Vehicle v5 = new Vehicle("FJB4E12", "MERCEDES-BENZ", "L-1113", "Azul", 1978, IpvaStatus.ISENTO, VehicleType.CAMINHAO, p3);

        // CASO: Pessoa com múltiplos veículos (carro + moto)
        Vehicle v6 = new Vehicle("NEG0I50", "TOYOTA", "COROLLA", "Preto", 2023, IpvaStatus.PAGO, VehicleType.CARRO, p2);

        vehicleRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5, v6));

        ScanHistory h1 = new ScanHistory();
        h1.setScannedPlate("QRM7E33");
        h1.setScanTimestamp(Instant.now().minusSeconds(3600));
        h1.setUser(standard);
        h1.setVehicle(v2);

        ScanHistory h2 = new ScanHistory();
        h2.setScannedPlate("DHM6197");
        h2.setScanTimestamp(Instant.now().minusSeconds(1800));
        h2.setUser(standard);
        h2.setVehicle(v4);

        ScanHistory h3 = new ScanHistory();
        h3.setScannedPlate("BRA2E19");
        h3.setScanTimestamp(Instant.now());
        h3.setUser(admin);
        h3.setVehicle(v3);

        scanHistoryRepository.saveAll(Arrays.asList(h1, h2, h3));
    }
}
