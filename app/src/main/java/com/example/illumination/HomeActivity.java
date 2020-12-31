package com.example.illumination;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import java.util.ArrayList;
import java.util.Objects;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class HomeActivity extends AppCompatActivity
{
    ArrayList<Book> bookList;
    BookListAdapter bookListAdapter;
    ListView list;
    EditText searchBooks;
    AdView adView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_home);

//        adView = findViewById(R.id.ad_view);
//        MobileAds.initialize(this,"ca-app-pub-5095893688489893~1033123996");
//        adView.setAdListener(new AdListener()
//        {
//            @Override
//            public void onAdLoaded()
//            {
//                Toast.makeText(HomeActivity.this, "Ad is Loaded.", Toast.LENGTH_SHORT).show();
//                adView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode)
//            {
//                Toast.makeText(HomeActivity.this, "Ad Failed to Load!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened()
//            {
//                Toast.makeText(HomeActivity.this, "Ad Opened.", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {}
//
//            @Override
//            public void onAdClosed() {}
//        });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//        bookList = new ArrayList<>();
//        list = findViewById(R.id.bookListView);
//        SetBookValues();
//        bookListAdapter = new BookListAdapter(HomeActivity.this, R.layout.activity_home_listview, bookList);
//        list.setAdapter(bookListAdapter);
//
//        searchBooks = findViewById(R.id.search);
//        searchBooks.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                bookListAdapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s){}
//        });
    }

//    void SetBookValues()
//    {
//        Book book1 = new Book();
//        book1.AddNewBook(1,"The Lost Girls Of Paris", "Pam Jenoff", "Historical Fiction", 800);
//        book1.SetDescription("Grace Healey is rebuilding her life after losing her husband during the war. One morning while passing through Grand Central Terminal on her way to work, she finds an abandoned suitcase tucked beneath a bench. Unable to resist her own curiosity, Grace opens the suitcase, where she discovers a dozen photographs—each of a different woman. In a moment of impulse, Grace takes the photographs and quickly leaves the station.\n" +
//                "\n" +
//                "Grace soon learns that the suitcase belonged to a woman named Eleanor Trigg, leader of a ring of female secret agents who were deployed out of London during the war. Twelve of these women were sent to Occupied Europe as couriers and radio operators to aid the resistance, but they never returned home, their fates a mystery. Setting out to learn the truth behind the women in the photographs, Grace finds herself drawn to a young mother turned agent named Marie, whose daring mission overseas reveals a remarkable story of friendship, valor and betrayal.\n" +
//                "\n" +
//                "Vividly rendered and inspired by true events, New York Times bestselling author Pam Jenoff shines a light on the incredible heroics of the brave women of the war, and weaves a mesmerizing tale of courage, sisterhood and the great strength of women to survive in the hardest of circumstances.\n");
//        bookList.add(book1);
//
//        Book book2 = new Book();
//        book2.AddNewBook(2, "Be A Blessing", "Debbie Macomber", "Self-Help", 450);
//        book2.SetDescription("Debbie Macomber has inspired readers for years with stories of love and resilience, creating characters who overcome all obstacles to become their best selves. Now Debbie shares the gifts and opportunities she has experienced as a lifelong journalist, guiding you through the process of cultivating your own creativity and well-being through journaling.\n" +
//                "\n" +
//                "Inside, you will find the perfect tools to jump-start your journey to a more mindful life:\n" +
//                "\n" +
//                "- Intuitive prompts to spark self-reflection\n" +
//                "- Insightful quotes and beautiful illustrations to inspire your creative side\n" +
//                "- Step-by-step guidance and encouragement from Debbie herself! \n");
//        bookList.add(book2);
//
//        Book book3 = new Book();
//        book3.AddNewBook(3, "Once A Liar", "A. F. Brady", "Mystery, Thriller, Suspense", 550);
//        book3.SetDescription("Peter Caine, a cutthroat Manhattan defense attorney, is extremely adept at his job. On the surface, he is charming and handsome, but inside he is cold and heartless. A sociopath practically incapable of human emotions, he has no remorse when he fights to acquit murderers, pedophiles and rapists. When Charlie Doyle, the daughter of the Manhattan DA--and Peter's former lover--is murdered, Peter's world is quickly sent into a tailspin as the DA, a professional enemy of Peter's, embarks on a witch hunt to avenge his daughter's death, stopping at nothing to ensure Peter is found guilty of the murder.\n" +
//                "\n" +
//                "Peter sets out to prove his innocence, and as he pieces together his defense, he finds that it's those closest to us who are capable of the greatest harm.\n");
//        bookList.add(book3);
//
//        Book book4 = new Book();
//        book4.AddNewBook(4,"The Institute", "Stephen King", "Horror, Fiction", 650);
//        book4.SetDescription("In the middle of the night, in a house on a quiet street in suburban Minneapolis, intruders silently murder Luke Ellis's parents and load him into a black SUV. The operation takes less than two minutes. Luke will wake up at The Institute, in a room that looks just like his own, except there’s no window. And outside his door are other doors, behind which are other kids with special talents—telekinesis and telepathy—who got to this place the same way Luke did: Kalisha, Nick, George, Iris, and ten-year-old Avery Dixon. They are all in Front Half. Others, Luke learns, graduated to Back Half, “like the roach motel,” Kalisha says. “You check in, but you don’t check out.”\n" +
//                "\n" +
//                "In this most sinister of institutions, the director, Mrs. Sigsby, and her staff are ruthlessly dedicated to extracting from these children the force of their extranormal gifts. There are no scruples here. If you go along, you get tokens for the vending machines. If you don’t, punishment is brutal. As each new victim disappears to Back Half, Luke becomes more and more desperate to get out and get help. But no one has ever escaped from the Institute.\n" +
//                "\n" +
//                "As psychically terrifying as Firestarter, and with the spectacular kid power of It, The Institute is Stephen King’s gut-wrenchingly dramatic story of good vs. evil in a world where the good guys don’t always win.\n");
//        bookList.add(book4);
//
//        Book book5 = new Book();
//        book5.AddNewBook(5,"The View From Alameda Island", "Robyn Carr", "Contemporary Romance", 850);
//        book5.SetDescription("From the outside looking in, Lauren Delaney has a life to envy—a successful career, a solid marriage to a prominent surgeon and two beautiful daughters who are off to good colleges. But on her twenty-fourth wedding anniversary Lauren makes a decision that will change everything.\n" +
//                "\n" +
//                "Lauren won’t pretend things are perfect anymore. She defies the controlling husband who has privately mistreated her throughout their marriage and files for divorce. And as she starts her new life, she meets a kindred spirit—a man who is also struggling with the decision to end his unhappy marriage.\n" +
//                "\n" +
//                "But Lauren’s husband wants his “perfect” life back and his actions are shocking. Facing an uncertain future, Lauren discovers an inner strength she didn’t know she had as she fights for the love and happiness she deserves.\n");
//        bookList.add(book5);
//
//        Book book6 = new Book();
//        book6.AddNewBook(6,"King Of Scars", "Leigh Bardugo", "Fantasy, Adventure, Fiction", 1250);
//        book6.SetDescription("Nikolai Lantsov has always had a gift for the impossible. No one knows what he endured in his country’s bloody civil war—and he intends to keep it that way. Now, as enemies gather at his weakened borders, the young king must find a way to refill Ravka’s coffers, forge new alliances, and stop a rising threat to the once-great Grisha Army.\n" +
//                "\n" +
//                "Yet with every day a dark magic within him grows stronger, threatening to destroy all he has built. With the help of a young monk and a legendary Grisha Squaller, Nikolai will journey to the places in Ravka where the deepest magic survives to vanquish the terrible legacy inside him. He will risk everything to save his country and himself. But some secrets aren’t meant to stay buried—and some wounds aren’t meant to heal.\n");
//        bookList.add(book6);
//
//        Book book7 = new Book();
//        book7.AddNewBook(7,"The Four Horsemen", "Dawkins, Dennett, Harris, Hitchens", "Controversial Literature", 700);
//        book7.SetDescription("At the dawn of the new atheist movement, the thinkers who became known as “the four horsemen,” the heralds of religion's unraveling—Christopher Hitchens, Richard Dawkins, Sam Harris, and Daniel Dennett—sat down together over cocktails. What followed was a rigorous, pathbreaking, and enthralling exchange, which has been viewed millions of times since it was first posted on YouTube. This is intellectual inquiry at its best: exhilarating, funny, and unpredictable, sincere and probing, reminding us just how varied and colorful the threads of modern atheism are.\n" +
//                "\n" +
//                "Here is the transcript of that conversation, in print for the first time, augmented by material from the living participants: Dawkins, Harris, and Dennett. These new essays, introduced by Stephen Fry, mark the evolution of their thinking and highlight particularly resonant aspects of this epic exchange. Each man contends with the most fundamental questions of human existence while challenging the others to articulate their own stance on God and religion, cultural criticism, spirituality, debate with people of faith, and the components of a truly ethical life. \n");
//        bookList.add(book7);
//
//        Book book8 = new Book();
//        book8.AddNewBook(8,"War Storm", "Victoria Aveyard", "Young-Adult, Fiction, Fantasy", 1050);
//        book8.SetDescription("Mare Barrow learned this all too well when Cal’s betrayal nearly destroyed her. Now determined to protect her heart—and secure freedom for Reds and newbloods like her—Mare resolves to overthrow the kingdom of Norta once and for all… starting with the crown on Maven’s head.\n" +
//                "\n" +
//                "But no battle is won alone, and before the Reds may rise as one, Mare must side with the boy who broke her heart in order to defeat the boy who almost broke her. Cal’s powerful Silver allies, alongside Mare and the Scarlet Guard, prove a formidable force. But Maven is driven by an obsession so deep, he will stop at nothing to have Mare as his own again, even if it means demolishing everything—and everyone—in his path.\n" +
//                "\n" +
//                "War is coming, and all Mare has fought for hangs in the balance. Will victory be enough to topple the Silver kingdoms? Or will the little lightning girl be forever silenced?\n" +
//                "\n" +
//                "In the epic conclusion to Victoria Aveyard’s stunning series, Mare must embrace her fate and summon all her power… for all will be tested, but not all will survive.\n");
//        bookList.add(book8);
//
//        Book book9 = new Book();
//        book9.AddNewBook(9,"A Curse So Dark And Lonely", "Brigid Kemmerer", "Young-Adult, Fiction, Fantasy", 900);
//        book9.SetDescription("Cursed by a powerful enchantress to repeat the autumn of his eighteenth year, Prince Rhen, the heir of Emberfall, thought he could be saved easily if a girl fell for him. But that was before he turned into a vicious beast hell-bent on destruction. Before he destroyed his castle, his family, and every last shred of hope.\n" +
//                "\n" +
//                "Nothing has ever been easy for Harper. With her father long gone, her mother dying, and her brother constantly underestimating her because of her cerebral palsy, Harper learned to be tough enough to survive. When she tries to save a stranger on the streets of Washington, DC, she's pulled into a magical world.\n" +
//                "\n" +
//                "Break the curse, save the kingdom.\n" +
//                "\n" +
//                "Harper doesn't know where she is or what to believe. A prince? A curse? A monster? As she spends time with Rhen in this enchanted land, she begins to understand what's at stake. And as Rhen realizes Harper is not just another girl to charm, his hope comes flooding back. But powerful forces are standing against Emberfall . . . and it will take more than a broken curse to save Harper, Rhen, and his people from utter ruin.\n");
//        bookList.add(book9);
//
//        Book book10 = new Book();
//        book10.AddNewBook(10,"Winter's Web", "Jennifer Estep", "Fantasy, Fiction", 650);
//        book10.SetDescription("An assassin at a renaissance faire. What could possibly go wrong? Everything, if you’re the Spider . . .\n" +
//                "\n" +
//                "I might be Gin Blanco, aka the assassin the Spider, but even I need a break from the bad guys every now and then. So when Owen Grayson, my significant other, suggests a trip to the Winter’s Web Renaissance Faire, it sounds like a perfect distraction from all my problems.\n" +
//                "\n" +
//                "The faire starts off innocently enough, but something seems slightly off about the cheery atmosphere and costumed characters. Maybe I’m being paranoid, but I can’t help but feel like I’m trapped in someone else’s icy web—and that they don’t want me to leave the faire alive . . .\n" +
//                "\n" +
//                "Note: Winter’s Web is a 27,000-word novella that takes place after the events of Venom in the Veins, book 17 in the Elemental Assassin urban fantasy series. Winter’s Web first appeared in the Seasons of Sorcery anthology in 2018.\n");
//        bookList.add(book10);
//
//        Book book11 = new Book();
//        book11.AddNewBook(11,"Crucible", "James Rollins", "Science-Fiction, Thriller, Suspense, Adventure", 700);
//        book11.SetDescription("In the race to save one of their own, Sigma Force must wrestle with the deepest spiritual mysteries of mankind in this mind-expanding adventure from the #1 New York Times bestselling author, told with his trademark blend of cutting edge science, historical mystery, and pulse-pounding action.\n" +
//                "\n" +
//                "Arriving home on Christmas Eve, Commander Gray Pierce discovers his house ransacked, his pregnant lover missing, and his best friend’s wife, Kat, unconscious on the kitchen floor. With no shred of evidence to follow, his one hope to find the woman he loves and his unborn child is Kat, the only witness to what happened. But the injured woman is in a semi-comatose state and cannot speak—until a brilliant neurologist offers a radical approach to “unlock” her mind long enough to ask a few questions.\n" +
//                "\n" +
//                "What Pierce learns from Kat sets Sigma Force on a frantic quest for answers that are connected to mysteries reaching back to the Spanish Inquisition and to one of the most reviled and blood-soaked books in human history—a Medieval text known as the Malleus Maleficarum, the Hammer of Witches. What they uncover hidden deep in the past will reveal a frightening truth in the present and a future on the brink of annihilation, and force them to confront the ultimate question: What does it mean to have a soul?\n");
//        bookList.add(book11);
//
//        Book book12 = new Book();
//        book12.AddNewBook(12,"Queen Of Air And Darkness", "Cassandra Clare", "Fantasy, Fiction", 1100);
//        book12.SetDescription("What if damnation is the price of true love?\n" +
//                "\n" +
//                "Innocent blood has been spilled on the steps of the Council Hall, the sacred stronghold of the Shadowhunters. In the wake of the tragic death of Livia Blackthorn, the Clave teeters on the brink of civil war. One fragment of the Blackthorn family flees to Los Angeles, seeking to discover the source of the disease that is destroying the race of warlocks. Meanwhile, Julian and Emma take desperate measures to put their forbidden love aside and undertake a perilous mission to Faerie to retrieve the Black Volume of the Dead. What they find in the Courts is a secret that may tear the Shadow World asunder and open a dark path into a future they could never have imagined. Caught in a race against time, Emma and Julian must save the world of Shadowhunters before the deadly power of the parabatai curse destroys them and everyone they love.\n" +
//                "\n");
//        bookList.add(book12);
//    }
//
//    @SuppressLint("RestrictedApi")
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        if(menu instanceof MenuBuilder)
//            ((MenuBuilder) menu).setOptionalIconsVisible(true);
//
//        getMenuInflater().inflate(R.menu.options_menu, menu);
//        menu.getItem(0).setEnabled(false);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case R.id.cartOption:
//                Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
//                startActivity(cartIntent);
//                return true;
//            case R.id.settingOption:
//                Intent accountSettingIntent = new Intent(HomeActivity.this, AccountSettingActivity.class);
//                startActivity(accountSettingIntent);
//                return true;
//            case R.id.viewOrderHistoryOption:
//                Intent orderHistoryIntent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
//                startActivity(orderHistoryIntent);
//                return true;
//            case R.id.logOutOption:
//                Intent logOutIntent = new Intent(HomeActivity.this, MainActivity.class);
//                logOutIntent.putExtra("LogOut", "LOGOUT");
//                getContentResolver().delete(CurrentUserProvider.CONTENT_URI, "", null);
//                startActivity(logOutIntent);
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onBackPressed()
//    {
//        Intent exitApp = new Intent(HomeActivity.this, MainActivity.class);
//        exitApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        exitApp.putExtra("EXIT", true);
//        startActivity(exitApp);
//    }
}