package xyz.brenden.firebase101;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<FriendlyMessage> friendlyMessageList;
    private static final String TAG = "MAIN SCREEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Query query = FirebaseFirestore.getInstance()
            .collection("intro-to-firebase")
            .orderBy("time")
            .limit(50);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                return;
            }
            List<FriendlyMessage> chats = snapshot.toObjects(FriendlyMessage.class);
            }
        });

        FirestoreRecyclerOptions<FriendlyMessage> options = new FirestoreRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(query, FriendlyMessage.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<FriendlyMessage, ChatHolder>(options) {
            @Override
            public void onBindViewHolder(ChatHolder holder, int position, FriendlyMessage model) {
                holder.message.setText(model.getMessage());
                holder.author.setText(" —" + model.getName());
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                    .inflate(R.layout.item_message, group, false);
                return new ChatHolder(view);
            }
        };

        //Setup Recycler View
        final RecyclerView rec = findViewById(R.id.messageRecyclerView);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(adapter);
        adapter.startListening();

        //Setup Message Sender
        final EditText msg = findViewById(R.id.messageEditText);
        final Button btn = findViewById(R.id.sendButton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FriendlyMessage data = new FriendlyMessage();
                data.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                data.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                data.setMessage(msg.getText().toString());
                data.setTime(System.currentTimeMillis());
                FirebaseFirestore.getInstance().collection("intro-to-firebase").add(data);

                msg.setText("");
            }
        });
    }
}
