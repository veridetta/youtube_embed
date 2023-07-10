package com.example.gvids.user;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gvids.R;
import com.example.gvids.adapter.PodcastUserAdapter;
import com.example.gvids.model.Podcast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PodcastFragment extends Fragment {

    private RecyclerView recyclerViewPodcasts;
    private PodcastUserAdapter podcastUserAdapter;
    private List<Podcast> podcastList;
    private FirebaseFirestore firestore;

    public PodcastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_podcast, container, false);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerViewPodcasts = view.findViewById(R.id.recyclerViewPrograms);
        //recyclerViewPodcasts.setLayoutManager(new LinearLayoutManager(getContext()));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewPodcasts.setLayoutManager(layoutManager);

        // Initialize podcast list
        podcastList = new ArrayList<>();

        // Get reference to the "podcasts" collection from Firestore
        CollectionReference podcastsRef = firestore.collection("podcasts");

        // Create query to retrieve all podcasts
        Query query = podcastsRef;

        // Retrieve podcast data based on the query
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Get the list of podcast documents from the query result
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Create Podcast object from the document data
                    Podcast podcast = documentSnapshot.toObject(Podcast.class);
                    podcastList.add(podcast);
                }

                // Initialize and set the adapter for RecyclerView
                podcastUserAdapter = new PodcastUserAdapter(getContext(), podcastList);
                recyclerViewPodcasts.setAdapter(podcastUserAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If failed to retrieve data from Firestore, display an error message
                Log.e("PodcastFragment", "Error getting podcasts", e);
            }
        });

        return view;
    }
}
