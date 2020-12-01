import static org.junit.jupiter.api.Assertions.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Filename: PackageManagerTest.java Project: p4 Authors: Elijah Asher
 * 
 * PackageManager is used to process json package dependency files and provide function that make
 * that information available to other users.
 * 
 * Each package that depends upon other packages has its own entry in the json file.
 * 
 * Package dependencies are important when building software, as you must install packages in an
 * order such that each package is installed after all of the packages that it depends on have been
 * installed.
 * 
 * For example: package A depends upon package B, then package B must be installed before package A.
 * 
 * This program will read package information and provide information about the packages that must
 * be installed before any given package can be installed. all of the packages in
 * 
 * You may add a main method, but we will test all methods with our own Test classes.
 */
class PackageManagerTest {
  static PackageManager manager;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    manager = new PackageManager();
  }

  @BeforeEach
  void setUp() throws Exception {
    manager = new PackageManager();

  }

  @Test
  void test_GetAllPackages() throws FileNotFoundException, IOException, ParseException {
    manager.constructGraph("shared_dependencies");
    Set<String> allpkg = manager.getAllPackages();
    if (allpkg.size() == 4)
      ;// expected
    else
      fail("ConstructGraph did not add the correct amount of verteces");
  }

  @Test
  void test_getInstallationOrder() throws CycleException, PackageNotFoundException,
      FileNotFoundException, IOException, ParseException {
    manager.constructGraph("shared_dependencies");
    List<String> list = manager.getInstallationOrder("A");
    if (list.size() > 0)
      fail("Nothing needs to be installed before A in shared_dependencies.json ");
  }

  @Test
  void test_InstallationOrderForAll() throws CycleException, PackageNotFoundException,
      FileNotFoundException, IOException, ParseException {
    manager.constructGraph("shared_dependencies");
    List<String> list = manager.getInstallationOrderForAllPackages();
    if (list.get(0).equals("A") && list.get(3).equals("D")) {
      // expected
    } else
      fail("Incorrect isntallation order");
  }

  @Test
  void test_PackageWithMaxDependencies() throws CycleException, PackageNotFoundException,
      FileNotFoundException, IOException, ParseException {
    manager.constructGraph("shared_dependencies");
    String mostDependent = manager.getPackageWithMaxDependencies();
    if (mostDependent.equals("A")) {
      // pass
    } else
      fail("Package with most depenencies was not returned");
  }
}
