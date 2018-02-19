package edu.mybooklistingappv2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by edu on 7/12/2017.
 */

public class BookClassAdapter extends ArrayAdapter<BookClass> {
    static class ViewHolder {
        private TextView titleTextView;
        private TextView authorTextView;
        private TextView dateTextView;
    }

    public BookClassAdapter(Activity context, ArrayList<BookClass> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        final BookClass currentBook = getItem(position);
        ViewHolder holder;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView) listItemView.findViewById(R.id.title);
            holder.authorTextView = (TextView) listItemView.findViewById(R.id.authors);
            holder.dateTextView = (TextView) listItemView.findViewById(R.id.date);

            listItemView.setTag(holder);

        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        holder.titleTextView.setText(currentBook.getTitle());
        holder.authorTextView.setText(currentBook.getAuthor());
        holder.dateTextView.setText(currentBook.getDate());

// TODO: ADD Image

        return listItemView;
    }
}
