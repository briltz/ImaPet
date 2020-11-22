package com.example.imapet.dummy;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.imapet.Login;
import com.example.imapet.Profile2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addItem(createDummyItem(count, document));
                                count++;
                            }
                        }
                    }
                });
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position, QueryDocumentSnapshot doc) {
        return new DummyItem(String.valueOf(position), doc.get("caption").toString(), doc.get("author").toString(), doc.get("image").toString(), doc.getId(), doc.get("location").toString());
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String image;
        public final String author;
        public final String location;
        public final String document;

        public DummyItem(String id, String content, String author, String image, String document, String location) {
            this.id = id;
            this.content = content;
            this.author = author;
            this.image = image;
            this.document = document;
            this.location = location;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}