import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Spielklasse des Spiels Snake.
 * <p>
 * Ziel dieses Spiels ist es alle Goldstuecke einzusammeln und
 * die Tuer zu erreichen, ohne sich selber zu beissen oder in
 * die Spielfeldbegrenzung reinzukriechen.
 */
public class SnakeSpiel
{
    private Schlange schlange;
    private Tuer tuer;
    private Spielfeld spielfeld;
    private List<Point> goldStuecke = new ArrayList<>();
    private int anzahlGoldStuecke = 0;
    private boolean spielLaeuft = true;
    private Scanner scanner;

    public static void main(String[] args)
    {
        new SnakeSpiel().spielen();
    }

    /**
     * Startet das Spiel.
     */
    public void spielen()
    {
        int anzahlGoldstuecke;
        scanner = new Scanner(System.in);
        System.out.println("Bitte anzahl Goldstuecke eingeben");
        anzahlGoldstuecke = scanner.nextInt();
        setAnzahlGoldstuecke(anzahlGoldstuecke);
        spielInitialisieren();
        while (spielLaeuft)
        {
            zeichneSpielfeld();
            ueberpruefeSpielstatus();
            fuehreSpielzugAus();
        }
        scanner.close();
    }


    private void spielInitialisieren()
    {

        tuer = new Tuer(0, 5);
        int hoehe = 10;
        int breite = 40;
        spielfeld = new Spielfeld(breite, hoehe);
        setzeGoldstuecke(anzahlGoldStuecke);
        schlange = new Schlange(30, 2);
    }

    private void setzeGoldstuecke(int anzahlGoldStuecke)
    {
        for (int i = 0; i < anzahlGoldStuecke; i++)
        {
            Point koordinategoldstueck = spielfeld.erzeugeZufallspunktInnerhalb();
            this.goldStuecke.add(koordinategoldstueck);
        }
    }

    private void zeichneSpielfeld()
    {
        char ausgabeZeichen;
        for (int y = 0; y < spielfeld.gibHoehe(); y++)
        {
            for (int x = 0; x < spielfeld.gibBreite(); x++)
            {
                Point punkt = new Point(x, y);
                ausgabeZeichen = '.';
                if (schlange.istAufPunkt(punkt))
                {
                    ausgabeZeichen = '@';
                } else if (istEinGoldstueckAufPunkt(punkt))
                {
                    ausgabeZeichen = '$';
                } else if (tuer.istAufPunkt(punkt))
                {
                    ausgabeZeichen = '#';
                }
                if (schlange.istKopfAufPunkt(punkt))
                {
                    ausgabeZeichen = 'S';
                }
                System.out.print(ausgabeZeichen);
            }
            System.out.println();
        }
    }

    private void setAnzahlGoldstuecke(int anzahlGoldstuecke)
    {
        this.anzahlGoldStuecke = anzahlGoldstuecke;
    }

    private void ueberpruefeSpielstatus()
    {
        if (istEinGoldstueckAufPunkt(schlange.gibPosition()))
        {
            Point standortGoldstueck = schlange.gibPosition();
            for (int i = 0; i < goldStuecke.size(); i++)
            {
                if (goldStuecke.get(i).equals(standortGoldstueck))
                {
                    goldStuecke.remove(i);
                }
            }
            schlange.wachsen();
            System.out.println("Goldstueck eingesammelt.");
        }
        if (istVerloren())
        {
            System.out.println("Verloren!");
            spielLaeuft = false;
        }
        if (istGewonnen())
        {
            System.out.println("Gewonnen!");
            spielLaeuft = false;
        }
    }


    private boolean istGewonnen()
    {
        return alleGoldstueckeGefressen() &&
                tuer.istAufPunkt(schlange.gibPosition());

    }

    private boolean alleGoldstueckeGefressen()
    {
        boolean alleGoldstueckegefressen = false;
        if (goldStuecke.isEmpty())
        {
            alleGoldstueckegefressen = true;
        }
        return alleGoldstueckegefressen;
    }

    private boolean istEinGoldstueckAufPunkt(Point punkt)
    {
        for (int i = 0; i < goldStuecke.size(); i++)
        {
            if (goldStuecke.get(i).equals(punkt))
            {
                return true;
            }
        }
        return false;
    }

    private boolean istVerloren()
    {
        return schlange.istKopfAufKoerper() ||
                !spielfeld.istPunktInSpielfeld(schlange.gibPosition());
    }

    private void fuehreSpielzugAus()
    {
        char eingabe = liesZeichenVonTastatur();
        schlange.bewege(eingabe);
    }

    private char liesZeichenVonTastatur()
    {
        char konsolenEingabe = scanner.next().charAt(0);
        return konsolenEingabe;
    }
}