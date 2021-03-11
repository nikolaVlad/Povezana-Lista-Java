package povezana_Lista;


import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PovezanaLista<T>  implements Iterable<T>
{   
   // Ugnjezdena klasa, koja predstavlja pojedinog elemenata Povezane Liste.
   static class ElementListe<T>
   {
       // Svojstva klase :
       
       // Promenljiva koja predstavlja samu vrednost elementa
       private T sadrzaj;
       // Referenca tekuceg elementa na sledeceg elemenata u Listi
       private ElementListe<T> sledeci;
       // Referenca tekuceg elementa na prethodni elemenat u Listi
       private ElementListe<T> prethodni;
       
       
       // Konstruktori :
       
       // Konstruktor - inicijalizuje sve 3 promenljive
       public ElementListe(T sadrzaj, ElementListe<T> sledeci, ElementListe<T> prethodni)
       {
           this.sadrzaj = sadrzaj;
           this.sledeci = sledeci;
           this.prethodni =  prethodni;
       }
       
       
       // Metode :
       
       // Prezentacija klase
       @Override 
       public String toString()
       {
           return "Sadrzaj : " + this.sadrzaj +
                   "Sledeci : " + this.sledeci +
                   "Prethodni : " + this.prethodni;
       }
   }
   
   
   // SVOJSTVA KLASE :
   
   // Glava liste - sluzi za kretanje kroz listu i oznake odakle krece lista
   // U pocetku su joj sva 3 svojstva null, jer je lista prazna
   // Kasnije ce reference - sledeci i prethodni pokazivati na prvi, odnosno
   // poslednji element u Listi.
   private ElementListe<T> glava = new ElementListe<T>(null,null,null);
   
   // Sluzi za velicinu liste, odnosno broj elemenata u listi
   // Inicijalna vrednost je 0, jer ne postoji nijedan element u listi
   // Sa svakim izmenom broja elemenata, ova promenljiva se azurira.
   private int velicina = 0;
   
   // KONSTRUKTORI :
   
   // Podrazumenavi - kreira praznu listu ( i u njoj je moguce dodati neogr. br el.)
   public PovezanaLista()
   {
       this.glava.prethodni = this.glava.sledeci = glava;
   }
   
   // Konstruktor - sa jednim arg tipa <T> - kreira Listu od 1 arg (ako nije null)
   public PovezanaLista(T sadrzaj)
   {
        if (sadrzaj != null)
        {
            ElementListe<T> noviEl = new ElementListe<T>(sadrzaj,glava,glava);
            glava.prethodni = noviEl;
            glava.sledeci = noviEl;
            velicina = 1;
        }
   }
   
   // Konstruktor - kreira listu od zadatog niza elemenata tipa <T>
   public PovezanaLista(T[] noviElementi)
   {
       // Kreiranje prazne liste, da bi ona mogla kasnije da se popuni
       this();
       dodajSve(noviElementi);
   }
   
   // METODI :
   
   // Prezentacija klase
   @Override
   public String toString()
   {
       StringBuilder rez = new StringBuilder();
       for(int i = 0; i <velicina; i++)
       {
          rez.append( (i< velicina -1) ? vrati(i) + " , " : vrati(i) + " ." );
       }
       return rez.toString();
   }
   
   
   // Dodavanje niza elemenata - krecuci sa odredjenog indeksa kao arg
   public boolean dodajSve(T[] noviElementi, int indeks) throws IndexOutOfBoundsException
   {
        // U slucaju da je zadati indeks u nedozvoljenim granicama
        if(indeks < 0 || indeks > this.velicina)
        {
            throw new IndexOutOfBoundsException("Indeks : " + indeks +
                                                "Velicina : " + velicina);
        }
        
        // Cuvanje broja novododatih elemenata
        int brojNovih = noviElementi.length;
        
        // Ako je broj dodati elemenata nula
        if(brojNovih == 0)
            return false; 
        
        // U slucaju da broj novih elemenata nije nula
        // krece dodavanje elemenata u Listi, jedan po jedan:
        
        // Kreiranje reference na prethodni i sledeci elemenat u tekucoj Listi,
        // radi dodavanja mogucnost umetanja elementata zadatih kao arg, u Listi.
        ElementListe<T> sledbenik = (indeks == velicina) ? glava : vratiElement(indeks);
        ElementListe<T> prethodnik = sledbenik.prethodni;
        // Prolaz kroz svih elemenata niza, datog kao arg
        for(int i = 0; i < brojNovih; i++)
        {
            // Kreiranje potpuno novog Elemenata Liste koji kao sadrzaj ima
            // vrednost i-tog elemenata iz Niza zadatog kao arg,
            // kao referencu na sledeci - onaj elemenat koji je sacuvan u @sledbenik
            // a kao referencu na prethodik - @prethodnik
            // Kroz for petlju prethodnik ce se menjati jer se ubacuju novi el.
            ElementListe<T> noviEl = new ElementListe<>(noviElementi[i],sledbenik,prethodnik);
            
            // Referenciranje sledbenika - prethodnika @novogEl na novonastali element Liste
            // da bi povezali Lanac i sa jedne i sa druge strane.
            noviEl.prethodni.sledeci = noviEl;
            
            // Promena prethodnika, da bi u sledecek for koraku sve bilo okej.
            prethodnik = noviEl;
        }
        // Po zavrsetka for petlje, sledbenik (poslednji element) dobija za referencu na
        // prethodni elementa upravo poslednje dodatog elementa u Listi odnosno @prethodnik.
        sledbenik.prethodni = prethodnik;
        velicina += brojNovih;
        return true;
   }
   
   // Dodavanje niza elemenata na kraj tekuce Liste
   public boolean dodajSve(T[] noviElementi)
   {
       return dodajSve(noviElementi,velicina);
   }
   
   // Vracanje elemenata sa zadatim indeksom kao arg
   public ElementListe<T> vratiElement(int indeks)
   {
       // Ako indeks nije u dozvoljenim granicama
       if (indeks < 0 || indeks >= this.velicina)
       {
           throw new IndexOutOfBoundsException("Indeks : " + indeks +
                                               " Velicina : " + velicina);
       }
       
       // Kreiranje tekuceg elemenata, koji ce biti referenciran na onaj elemenat
       // sa zadatim @indeks-om kao argumenat
       // Inicijalno referenciran na glavu, jer se od nje vrsi kretanje kroz Listu.
       ElementListe<T> tekuci = glava;
       
       // Ispitivanje da li je kojoj polovini velicine je @indeks blizi:
       
       // Ako se indeks nalazi izmedju 0 i velicina/2:
       if(indeks < (velicina >> 1))
       {
           // Kretanje do zadatog @indeksa pomocu for petlje i reference na @sledeci
           // elemenat poceci od @glava
           for(int i = 0; i <= indeks; i++)
           {
               tekuci = tekuci.sledeci;
           }
       }
       
       // U suprotnom - ako je indeks izmedju velicina/2 i velicina
       else
       {
           // Kretanje od zadnjeg elemenata do indeksa pomocu for petle i reference 
           // na @prethodni elemenat poceci od @glava
           for(int i = velicina -1; i >= indeks; i--)
           {
               tekuci = tekuci.prethodni;
           }
       }
       
       return tekuci;
   }
   
   // Vracanje @sadrzaj elemenata sa datim indeksom kao arg
   public T vrati(int indeks)
   {
       return vratiElement(indeks).sadrzaj;
   }
   
   // Umetanje novog elemenata na zadati indeks
   public ElementListe<T> dodajPre(T sadrzaj, ElementListe<T> e)
   {
       ElementListe<T> noviEl = new ElementListe<T>(sadrzaj,e,e.prethodni);
       e.prethodni.sledeci = noviEl;
       e.prethodni = noviEl;
       velicina++;
       return noviEl;
   }
   
   
   // Umetanje elementa na kraj tekuce Liste
   public ElementListe<T> dodajElement(T sadrzaj)
   {
       return dodajPre(sadrzaj,glava);
   }
   
   
   // Vracanje velicine tekuce Liste.
   public int vratiVelicinu()
   {
       return this.velicina;
   }
   
   // Vracanje prvog elementa u listi
   public T vratiPrvi()
   {
       if (this.velicina == 0)
           throw new NoSuchElementException();
       
       return glava.sledeci.sadrzaj;
   }
   
   // Vracanje poslednjeg elementa u listi
   public T vratiPoslednji()
   {
       if (this.velicina == 0)
           throw new NoSuchElementException();
       
       return glava.prethodni.sadrzaj;
   }
   
   
   // NOVI METODI:
   
   
   // Brisanje elementa sa datim ineksom
   public ElementListe<T> obrisi(int indeks) throws IndexOutOfBoundsException
   {
       // Provera validnosti indeksa
       if(indeks < 0 || indeks >= velicina)
       {
           throw new IndexOutOfBoundsException("Indeks : " + indeks + ","
                                             + " Velicina : " + this.velicina);
       }
       
       // Referenciranje elementa koji ce biti obrisan
       ElementListe<T> obrisani = this.vratiElement(indeks);
       
       // Referenciranje prethodnika obrisanog el
       obrisani.prethodni.sledeci = obrisani.sledeci;
       
       // Referenciranje sledbenika obrisanog el
       obrisani.sledeci.prethodni = obrisani.prethodni;
       
       // Azuriranje velicine 
       velicina--;
       
       // Vracanje obrisanog elementa
       return obrisani;
   }
   
   // Brisanje poslednjeg elementa u Listi
   public ElementListe<T> obrisiPoslednji()
   {
       // Koristnje kreirane Metode @obrisi(int indeks) s tim sto se kao indeks
       // zadaje poslednji element u Listi
       return obrisi(this.velicina-1);
   }
   
   // Brisanje elemenata od indeksa zadatog kao elemenat do kraja
   public int obrisiVise(int startIndeks) throws IndexOutOfBoundsException
   {
       return obrisiVise(startIndeks, velicina);
   }

    /**
     *Brisanje elemenata od pocentog do krajnjeg indeksa, s tim sto u brisanju
     *ulazi indeks pocetka (@startIndeks), a ne i indeks kraja (@krajIndeks)
     *Pa elementi koji su obrisani su od starIndeks do krajIndeks-1.
     * @param startIndeks
     * @param krajIndeks
     * @return
     * @throws IndexOutOfBoundsException
     */
   public int obrisiVise(int startIndeks, int krajIndeks) throws IndexOutOfBoundsException
   {
       // Provera validnosti pocetnog indeksa
       if (startIndeks < 0 || startIndeks > this.velicina)
       {
           throw new IndexOutOfBoundsException("Pocetni indeks : " + startIndeks +
                                               ", Velicina : " + this.velicina);
       }
       
       // Provera validnosti krajnjeg indeksa
       if(startIndeks < 0 || startIndeks > this.velicina)
       {
           throw new IndexOutOfBoundsException("Krajni indeks : " + krajIndeks +
                                               ", Velicina : " + this.velicina);
       }
       
       // Broj elemenata koji ce potencijalno biti obrisani
       int brojObrisanih = krajIndeks - startIndeks;
       
       // Ako je broj elemenata koji ce biti obrisani negativan
       if(brojObrisanih < 0)
       {
           return brojObrisanih;
       }
       
       
       // Referenca na prvi izabrani elemenat pomocu indeksa zadat kao arg.
       // koji ce biti obrisan.
       ElementListe<T> prviIzabrani = this.vratiElement(startIndeks);
       
       // Referenciranje na poslednjeg elementa pomocu indeksa zadat kao arg,
       // koji ce biti obrisan.
       ElementListe<T> poslednjiIzabrani = this.vratiElement(krajIndeks - 1);
       
       //Izbacivanje elemenata izabranih pomocu indeksa, iz Liste
       prviIzabrani.prethodni.sledeci = poslednjiIzabrani.sledeci;
       poslednjiIzabrani.sledeci.prethodni = prviIzabrani.prethodni;
       
       // Azuriranje velicine  
       this.velicina = this.velicina - brojObrisanih;
       
       // Vracanje broja obrisanih elemenata ako je oon pozitivan
       return brojObrisanih;
   }
   
   // Brisanje svih elemenata u listi
   public void isprazni()
   {
       this.glava = new ElementListe(null,null,null);
       this.glava.sledeci = this.glava.prethodni = glava;
       this.velicina = 0;
   }
   
   // Konstruktor - KLON
   public PovezanaLista(PovezanaLista<T> lista)
   {
       // Kreiranje prazne liste;
       this();
       // Referenciranje na elementa u Listi zadatoj kao arg,
       // pomocu kojeg se dodaju novi el u tekucoj listi
       ElementListe<T> noviEl = lista.glava;
       
       // Prolaz kroz svaki elemenat zadate Liste kao arg
       for(int i = 0; i < lista.velicina; i++)
       {
           // Referenciranje na elementa sledbenika u Listi zadatoj kao arg
           // da bi se proslo kroz svaki elemenat te Liste
           noviEl = noviEl.sledeci;
           // Dodavanje sadrzaja iz Elementa u listi zadatoj kao arg u 
           // tekucoj listi
           this.dodajElement(noviEl.sadrzaj);
       }
   }
   
   // Kloniranje trenutne liste
   public PovezanaLista<T> kloniraj()
   {
       return new PovezanaLista(this);
   }
   
   // Pravljene od Liste -> Niz 1
   public T[] vratiNiz(Object[] niz)
   {
       // Referenca na glavi tekuce liste, da bi se kretali kroz Listu
       ElementListe<T> refGlave = this.glava;
       // Cuvanje elemenata Liste u Niz
       for(int i = 0; i < niz.length; i++)
       {
           refGlave = refGlave.sledeci;
           niz[i] = refGlave.sadrzaj;
       }
       // Vracanje punog niza
       return (T[]) niz;
   }
   
   // Dodavanje svih elemenata liste koja je zadata kao arg
   public boolean dodajSve(PovezanaLista<? extends T> novaLista) throws IndexOutOfBoundsException
   {
       return dodajSve(novaLista,velicina);
   }
   
   // Dodavanje svih elemenata liste koja je zadata kao arg, sa pocetnim indeksom
   public boolean dodajSve(PovezanaLista<? extends T> novaLista, int indeks) throws IndexOutOfBoundsException
   {
        Object niz = new Object[novaLista.velicina];
        niz = novaLista.vratiNiz((T[]) niz);
        return dodajSve((T[]) niz,indeks);
   }
   
   // Zamena sadrzaja datog elementa
   public T zameni(T noviSadrzaj,int indeks) throws IndexOutOfBoundsException
   {
       // U slucaju da je zadati indeks u nedozvoljenim granicama
       if (indeks < 0 || indeks >= velicina)
       {
           throw new IndexOutOfBoundsException("Indeks : " + indeks +
                                                "Velicina : " + velicina);
       }
       // Promena sadrzaja zadatog indeksa
       return this.vratiElement(indeks).sadrzaj = noviSadrzaj;
   }
   
   // Zamena sadrzaja niza Elemenata u Listi, pocetkom sa odredjenog indeksa kao arg
   public boolean zameniVise(T[] noviEl, int StartIndeks) throws IndexOutOfBoundsException
   {
       // U slucaju da je zadati indeks u nedozvoljenim granicama
       if(StartIndeks < 0 || StartIndeks >= this.velicina)
       {
           throw new IndexOutOfBoundsException("Indeks : " + StartIndeks +
                                                "Velicina : " + this.velicina);
       }
       
       // U slucaju da broj elemenata zadatih kao arg i indeks pocetnog elemenata
       // za promeni, prevazilaze velicinu tekuce liste
       if ( (noviEl.length + StartIndeks) > this.velicina)
       {
           // Vraca false
           return false;
       }
       
       // Zamena elemenata jedan po jedan
       for(int i = 0; i < noviEl.length; i++)
       {
           this.zameni(noviEl[i], StartIndeks);
           StartIndeks++;
       }
       return true;
   }
   
   // Zamena sadrzaja niza Elemenata iz jedne Lista zadate kao arg
   // u tekucoj listi.
   public boolean zameniVise(PovezanaLista<T> lista, int startIndeks) throws IndexOutOfBoundsException
   {
        Object niz = new Object[lista.vratiVelicinu()];
        niz = lista.vratiNiz((T[]) niz);
       
        return zameniVise( (T[]) niz,startIndeks);
   }
   
   // * Stampanje Liste
   public void stamp()
   {
       System.out.println(this);
   }
   
   // * Stampanje Liste sa rednim brojem stampanja
   public void stamp(int br)
   {
        System.out.println("Stamp br. " + br + " : " + this);
   }
  
   // Vracanje indeksa 1. pojavljivanja
   // elementa zadatog kao arg
   public int indeksOd(Object o)
   {
       if (o == null)
       {
           return -1;
       }
       
       // referenca glave
       ElementListe<T> refGlave = glava;
       
       for(int i = 0; i < this.velicina; i++)
       {
           if (o == refGlave.sledeci.sadrzaj)
           {
               return i;
           }
           refGlave = refGlave.sledeci;
       }
       
       return -1; 
   }
   
   // Vracanje indeksa poslednjeg pojavljivanja
   // elementa zadatog kao arg
   public int zadnjiIndeksOd(Object o)
   {
       if (o == null)
       {
           return -1;
       }
       
       // Referenca glave
       ElementListe<T> refGlave = glava;
       
       for(int i = velicina - 1; i >= 0; i--)
       {
           if (o == refGlave.prethodni.sadrzaj)
           {
               return i;
           }
           refGlave = refGlave.prethodni;
       }
       return -1;
   }

    // P R E P I S A N I metod interfejsa @Iterable<T>
    private ElementListe<T> trenutni = glava; // Radi prolaz kroz Listu, pomocu Iteratora
   @Override
   public Iterator<T> iterator()
    {
            // Kreiranje anonimne klase Iterator
           return  new Iterator<T>() {
           // Promena definicije metoda    
           @Override
           public boolean hasNext() 
           {
               if (trenutni.sledeci == glava)
               {
                   trenutni = glava;
                   return false;
               }
               else 
               {
                   return true;
               }
           }

           @Override
           public T next() 
           {
                //trenutni = trenutni.sledeci;
                return  (trenutni = trenutni.sledeci).sadrzaj;
           }
       };
   }
   
   // Proveravanje dveju lista, da li su jednake (isti broj el. i isti sadrzaj elemenata)
    public boolean uporedi(PovezanaLista<T> lista)
    {
        
       // Proveravanje duzina : 
       if (this.velicina != lista.velicina)
           return false;
        
       // Proveravanje sadrzaja :
       
       // Referenciranje glava, jedne i druge Liste
       ElementListe<T> glavaTekuca = this.glava;
       ElementListe<T> glavaArgument = (ElementListe<T>) lista.glava;
       
       for(int i = 0; i < this.velicina; i++)
       {
           if((glavaTekuca = glavaTekuca.sledeci).sadrzaj != (glavaArgument=glavaArgument.sledeci).sadrzaj)
               return false;
       }
       
       return true;
    }
    
}  



