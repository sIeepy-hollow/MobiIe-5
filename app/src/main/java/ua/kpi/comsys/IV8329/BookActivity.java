package ua.kpi.comsys.IV8329;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BookActivity extends AppCompatActivity {

    private static Book book;

    private static ImageButton back_button;

    private static ImageView cover;
    private static TextView title;
    private static TextView subtitle;
    private static TextView isbn;
    private static TextView price;

    private static TextView authors;
    private static TextView publisher;
    private static TextView year;
    private static TextView pages;
    private static TextView rating;
    private static TextView description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        book = (Book) bundle.getParcelable("book");
        setContentView(R.layout.activity_book);

        back_button = findViewById(R.id.back_btn);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String tmp;
        if (book.getImage() != null && !book.getImage().isEmpty()) {
            cover = findViewById(R.id.book_cover);
            findViewById(R.id.cover_text).setVisibility(View.INVISIBLE);
            tmp = book.getImage();
            cover.setImageResource(getResources().getIdentifier(tmp.substring(0, tmp.lastIndexOf(".")),
                    "raw", getPackageName()));
        }
        if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            title = findViewById(R.id.book_title);
            title.setText(book.getTitle());
            title.setTextColor(getResources().getColor(R.color.black));
        }
        if (book.getSubtitle() != null && !book.getSubtitle().isEmpty()) {
            subtitle = findViewById(R.id.book_subtitle);
            subtitle.setText(book.getSubtitle());
            subtitle.setTextColor(getResources().getColor(R.color.black));
        }
        if (book.getPrice() != null && !book.getPrice().isEmpty()) {
            price = findViewById(R.id.book_price);
            price.setText(book.getPrice());
            price.setTextColor(getResources().getColor(R.color.black));
        }
        if (book.getIsbn13() != null && !book.getIsbn13().isEmpty()) {
            isbn = findViewById(R.id.book_isbn);
            tmp = getResources().getString(R.string.isbn) + book.getIsbn13();
            isbn.setText(tmp);
            isbn.setTextColor(getResources().getColor(R.color.black));
            book = findExtraInfo(book);
            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                authors = findViewById(R.id.book_authors);
                authors.setText(book.getAuthors());
                authors.setTextColor(getResources().getColor(R.color.black));
            }
            if (book.getPublisher() != null && !book.getPublisher().isEmpty()) {
                publisher = findViewById(R.id.book_publisher);
                publisher.setText(book.getPublisher());
                publisher.setTextColor(getResources().getColor(R.color.black));
            }
            if (book.getYear() != null && !book.getYear().isEmpty()) {
                year = findViewById(R.id.book_year);
                year.setText(book.getYear());
                year.setTextColor(getResources().getColor(R.color.black));
            }
            if (book.getPages() != null && !book.getPages().isEmpty()) {
                pages = findViewById(R.id.book_pages);
                pages.setText(book.getPages());
                pages.setTextColor(getResources().getColor(R.color.black));
            }
            if (book.getRating() != null && !book.getRating().isEmpty()) {
                tmp = book.getRating() + getResources().getString(R.string.out_of_five);
                rating = findViewById(R.id.book_rating);
                rating.setText(tmp);
                rating.setTextColor(getResources().getColor(R.color.black));
            }
            if (book.getDescription() != null && !book.getDescription().isEmpty()) {
                description = findViewById(R.id.book_description);
                description.setText(book.getDescription());
                description.setTextColor(getResources().getColor(R.color.black));
            }
        }

    }

    private Book findExtraInfo(Book book) {
        String filename = "i" + book.getIsbn13();
        int id = getResources().getIdentifier(filename, "raw", getPackageName());
        if (id != 0) {
            InputStream inputStream = getResources().openRawResource(id);
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
            String eachline = null;
            String currBook = "";
            try {
                eachline = bufferedReader.readLine();
                while (eachline != null) {
                    currBook += eachline;
                    eachline = bufferedReader.readLine();
                }
                int elementStart;
                currBook = currBook.substring(currBook.indexOf("{")+1, currBook.indexOf("}"));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"authors\":")+"\"authors\":".length());
                book.setAuthors(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"publisher\":")+"\"publisher\":".length());
                book.setPublisher(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"year\":")+"\"year\":".length());
                book.setYear(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"pages\":")+"\"pages\":".length());
                book.setPages(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"rating\":")+"\"rating\":".length());
                book.setRating(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"desc\":")+"\"desc\":".length());
                book.setDescription(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return book;
    }
}
