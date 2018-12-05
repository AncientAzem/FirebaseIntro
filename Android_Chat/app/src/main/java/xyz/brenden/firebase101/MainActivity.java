package xyz.brenden.firebase101;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Startup Handler
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Query for Firestore
        Query query = FirebaseFirestore.getInstance()
            .collection("intro-to-firebase") // Loads in Collection
            .orderBy("time") // Sorts by 'time' field
            .limit(50); // Retrieves the top 50 results

        // Query and Listen for Updates on Query
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

            //Handle if Snapshot is 'null'
            if (e != null) {
                return;
            }

            // Converts Firestore Object to Custom Class
            List<FriendlyMessage> chats = snapshot.toObjects(FriendlyMessage.class);
            }
        });

        // Setup Firestore Recycler Options
        FirestoreRecyclerOptions<FriendlyMessage> options = new FirestoreRecyclerOptions.Builder<FriendlyMessage>()
                .setQuery(query, FriendlyMessage.class) // Assigns Query
                .build(); // Saves Settings

        // Setup Adapter to Handle Chat Messages
        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<FriendlyMessage, ChatHolder>(options) {

            // Assigns Firestore Data to ViewHolder
            @Override
            public void onBindViewHolder(ChatHolder holder, int position, FriendlyMessage model) {
                holder.message.setText(model.getMessage());
                holder.author.setText(" —" + model.getName());
            }

            // Inflates the ViewHolder
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                    .inflate(R.layout.item_message, group, false);
                return new ChatHolder(view);
            }
        };

        // Bind RecyclerView to Adapter
        final RecyclerView rec = findViewById(R.id.messageRecyclerView);
        rec.setLayoutManager(new LinearLayoutManager(this));
        rec.setAdapter(adapter);
        adapter.startListening();

        // Setup Message Sender
        final EditText msg = findViewById(R.id.messageEditText);
        final Button btn = findViewById(R.id.sendButton);

        // Save New Messages in Firestore
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FriendlyMessage data = new FriendlyMessage();
                data.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                data.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                data.setMessage(msg.getText().toString());
                data.setTime(System.currentTimeMillis());
                FirebaseFirestore.getInstance().collection("intro-to-firebase").add(data);

                // Reset Form
                msg.setText("");
            }
        });
    }
}
