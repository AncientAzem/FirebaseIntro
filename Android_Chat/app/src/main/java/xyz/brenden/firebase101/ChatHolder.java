package xyz.brenden.firebase101;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ChatHolder extends RecyclerView.ViewHolder {

    public final TextView message;
    public final TextView author;
    public final CardView card;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.msg);
        author = itemView.findViewById(R.id.author);
        card = itemView.findViewById(R.id.cardView);
    }
}
