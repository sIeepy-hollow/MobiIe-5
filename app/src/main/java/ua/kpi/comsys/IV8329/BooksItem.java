package ua.kpi.comsys.IV8329;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class BooksItem extends Fragment implements BooksView.BookListener {

    private static RecyclerView recyclerView;
    private static BooksView adapter;
    private SearchView booksSearch;
    private static TextView no_result;
    private static ArrayList<Book> books;
    private static ArrayList<Book> books_result;
    private FloatingActionButton addBookBtn;
    int addBookLaunch = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        books = getBooks();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_books, container, false);

        books_result = new ArrayList<>(books);
        recyclerView = root.findViewById(R.id.books_list);
        adapter = new BooksView(this.getContext(), books_result, this);
        recyclerView.setAdapter(adapter);
        booksSearch = root.findViewById(R.id.books_search);
        no_result = root.findViewById(R.id.noRes);
        no_result.setVisibility(View.INVISIBLE);
        booksSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                no_result.setVisibility(View.INVISIBLE);
                String query = newText.toLowerCase();
                books_result = new ArrayList<>();
                for(int i=0; i<books.size(); i++) {
                    if (books.get(i).getTitle().toLowerCase().contains(query)) {
                        books_result.add(books.get(i));
                    }
                }
                adapter = new BooksView(booksSearch.getContext(), books_result, BooksItem.this);
                recyclerView.setAdapter(adapter);
                if (books_result.size() == 0) {
                    no_result.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        addBookBtn = root.findViewById(R.id.addBookBtn);
        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addBookBtn.getContext(), AddBookActivity.class);
                startActivityForResult(intent, addBookLaunch);
            }
        });
        return root;
    }

    ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.books_list);
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String eachline = null;
        String booksStr = "";
        try {
            eachline = bufferedReader.readLine();
            while (eachline != null) {
                booksStr += eachline;
                eachline = bufferedReader.readLine();
            }
            booksStr = booksStr.substring(booksStr.indexOf("[")+1, booksStr.lastIndexOf("]"));
            String currBook;
            Book book = new Book();
            int elementStart;
            while (!booksStr.isEmpty()) {
                currBook = booksStr.substring(booksStr.indexOf("{")+1, booksStr.indexOf("}"));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"title\":")+"\"title\":".length());
                book.setTitle(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"subtitle\":")+"\"subtitle\":".length());
                book.setSubtitle(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"isbn13\":")+"\"isbn13\":".length());
                book.setIsbn13(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"price\":")+"\"price\":".length());
                book.setPrice(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                elementStart = currBook.indexOf("\"", currBook.indexOf("\"image\":")+"\"image\":".length());
                book.setImage(currBook.substring(elementStart+1, currBook.indexOf("\"", elementStart+1)));
                books.add(book);
                book = new Book();
                booksStr = booksStr.substring(booksStr.indexOf("}") + 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return books;
    }

    @Override
    public void onBookClick(int position) {
        Intent intent = new Intent(this.getContext(), BookActivity.class);
        intent.putExtra("book", books_result.get(position));
        startActivity(intent);
    }

    @Override
    public void onBookHold(int position) {
        new AlertDialog.Builder(this.getContext())
                .setTitle(this.getContext().getResources().getString(R.string.delete_confirm))
                .setMessage("Do you really want to delete \"" + books_result.get(position).getTitle() + "\"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Book rmBook = books_result.get(position);
                        for (int i=0; i<books.size(); i++) {
                            if (books.get(i).getIsbn13().equals(rmBook.getIsbn13())) {
                                books.remove(i);
                            }
                        }
                        books_result.remove(position);
                        adapter = new BooksView(booksSearch.getContext(), books_result, BooksItem.this);
                        recyclerView.setAdapter(adapter);
                        if (books_result.size() == 0) {
                            no_result.setVisibility(View.VISIBLE);
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }


    private void addBook(Book newBook) {
        this.books.add(newBook);
        if (this.booksSearch.getQuery().toString().isEmpty() || newBook.getTitle().toLowerCase().contains(this.booksSearch.getQuery().toString().toLowerCase())) {
            books_result.add(newBook);
            adapter = new BooksView(booksSearch.getContext(), books_result, this);
            recyclerView.setAdapter(adapter);
            if (books_result.size() == 0) {
                no_result.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == addBookLaunch) {
            if (resultCode == Activity.RESULT_OK) {
                Book newBook = data.getParcelableExtra("book");
                this.addBook(newBook);
            }
        }
    }

}