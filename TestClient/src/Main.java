public class Main
{
    public static void main(String[] args)
    {
        ClientPyRA clientPyRA = new ClientPyRA("158.255.93.206", "9642");

        boolean udalo_sie = clientPyRA.login("Michaltyk2", "JestemHashem");
        //boolean doszedl = clientPyRA.sendSkin("Michaltyk2", new byte[1234]);
        //String odpowiedz = clientPyRA.runScript("test.py", "abacki babacki cabacki");
        //clientPyRA.hardReset();

        System.out.println(udalo_sie);
    }
}
