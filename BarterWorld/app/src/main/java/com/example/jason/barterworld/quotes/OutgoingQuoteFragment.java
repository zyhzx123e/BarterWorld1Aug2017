package com.example.jason.barterworld.quotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jason.barterworld.R;
import com.example.jason.barterworld.model.OutgoingQuote;
import com.example.jason.barterworld.tools.RoundedTransformation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OutgoingQuoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutgoingQuoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutgoingQuoteFragment extends Fragment  implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private DatabaseReference fire_db_outgoing_quote_db;
private  DatabaseReference mOutgoinfQuoteDb;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public String user_uid="";
    public String display_uid="";

    private FirebaseAuth user_auth;

    private FirebaseUser currentUser;
    public String msg_key;

   // private OnFragmentInteractionListener mListener;

    private Context c;

    private LinearLayoutManager manager;

    public OutgoingQuoteFragment() {
        // Required empty public constructor
        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        // fire_db_outgoing_quote_db= FirebaseDatabase.getInstance().getReference().child("Outgoing_Quotes").child(user_uid);
        fire_db_outgoing_quote_db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://barterworld-ad75e.firebaseio.com/");


        mOutgoinfQuoteDb =fire_db_outgoing_quote_db.child("Outgoing_Quotes").child(user_uid);
        mOutgoinfQuoteDb.keepSynced(true);

    }


    // TODO: Rename and change types and number of parameters
  /*  public static OutgoingQuoteFragment newInstance(String param1, String param2) {
        OutgoingQuoteFragment fragment = new OutgoingQuoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    /*    if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/


        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        fire_db_outgoing_quote_db= FirebaseDatabase.getInstance().getReference().child("Outgoing_Quotes").child(user_uid);

        fire_db_outgoing_quote_db.keepSynced(true);

        //adapter_setView();
    }

    @Override
    public void onStart() {
        super.onStart();
        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();
        //Initializes Recycler View and Layout Manager.

        fire_db_outgoing_quote_db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://barterworld-ad75e.firebaseio.com/");

        mOutgoinfQuoteDb =fire_db_outgoing_quote_db.child("Outgoing_Quotes").child(user_uid);


        mOutgoinfQuoteDb.keepSynced(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter_setView();



    }

    public void adapter_setView(){

        FirebaseRecyclerAdapter<OutgoingQuote,OutgoingQuoteFragment.OutgoingQuoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OutgoingQuote,OutgoingQuoteFragment.OutgoingQuoteViewHolder>(
                OutgoingQuote.class,
                R.layout.outgoing_quote_row,
                OutgoingQuoteFragment.OutgoingQuoteViewHolder.class,
                mOutgoinfQuoteDb //change to   query_current_user_post

        ) {
            @Override
            protected void populateViewHolder(OutgoingQuoteFragment.OutgoingQuoteViewHolder viewHolder, OutgoingQuote model, int position) {

                final String post_key = getRef(position).getKey();

                msg_key = model.getOutgoing_msg();

                viewHolder.setBarterTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                //  Toast.makeText(MainActivity.this,model.getDescription()+"--"+model.getTitle()+ "test descrip",Toast.LENGTH_SHORT);
                viewHolder.setType(model.getType());
                viewHolder.setVal(model.getValue());
                viewHolder.setUser(model.getUsername());

                viewHolder.setLikeCount(model.getLike_count());
                viewHolder.setImg(getActivity().getApplicationContext(),model.getBarter_img());



                viewHolder.outgoing_quote_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"This is the "+model.getTitle()+" - "+post_key+"",Toast.LENGTH_LONG).show();
                        //click on each barter item

                        Intent Chat_Intent = new Intent(getActivity(), com.example.jason.barterworld.ChatActivity.class);

                        Chat_Intent.putExtra("msg_key",msg_key);

                        startActivity(Chat_Intent);



                    }
                });





            }
        };


  /*      firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        firebaseRecyclerAdapter.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    firebaseRecyclerAdapter.scrollToPosition(positionStart);
                }
            }
        });
*/
        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view =  inflater.inflate(R.layout.fragment_outgoing_quote, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.outgoing_quote_recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        fire_db_outgoing_quote_db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://barterworld-ad75e.firebaseio.com/");


          mOutgoinfQuoteDb =fire_db_outgoing_quote_db.child("Outgoing_Quotes").child(user_uid);
        mOutgoinfQuoteDb.keepSynced(true);

//        Toast.makeText(c, "Hi, this", Toast.LENGTH_SHORT).show();
       // c = getContext();

        //Initializes Recycler View and Layout Manager.
        //recyclerView = (RecyclerView) view.findViewById(R.id.outgoing_quote_recycler_list);
       // manager = new LinearLayoutManager(c);




            //recyclerView.setHasFixedSize(true);
           // recyclerView.setLayoutManager(manager);
       // manager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter_setView();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onClick(View v) {

    }


    //------------------------------------------------------view_holder class-------------------------------

    public static class OutgoingQuoteViewHolder extends RecyclerView.ViewHolder{

        FirebaseAuth user_auth;

        View outgoing_quote_view;


        public OutgoingQuoteViewHolder(View itemView) {
            super(itemView);
            outgoing_quote_view=itemView;

            user_auth = FirebaseAuth.getInstance();

        }


        public void setBarterTitle(String title){

            TextView txt_title= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_name);
            txt_title.setText(title);
        }




        public void setDescription(String desc){

            TextView txt_desc= (TextView)outgoing_quote_view.findViewById(R.id.txt_comment);
            txt_desc.setText(desc);

        }
        public void setType(String type){

            TextView txt_type= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_type);
            txt_type.setText(type);

        }
        public void setVal(String value){

            TextView txt_value= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_value);
            txt_value.setText("$value "+value);

        }
        public void setUser(String user){

            TextView txt_user= (TextView)outgoing_quote_view.findViewById(R.id.txt_post_user);
            txt_user.setText("Owner : "+user);

        }

        public void setLikeCount(String count){

            TextView txt_likeCunt= (TextView)outgoing_quote_view.findViewById(R.id.txt_likes_num);
            txt_likeCunt.setText("Liked by "+count+" people");

        }

        public void setImg(final Context ctx, final String img){

            final ImageView post_img = (ImageView)outgoing_quote_view.findViewById(R.id.img_post);
            //Picasso.with(ctx).load(img).into(post_img);

            Picasso.with(ctx).load(img).fit()
                    .placeholder(R.drawable.ic_media_play)
                    .centerCrop()
                    .transform(new RoundedTransformation(10, 4))
                    .into(post_img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ctx).load(img).fit()
                                    .placeholder(R.drawable.ic_media_play).centerCrop()
                                    .transform(new RoundedTransformation(10, 4))
                                    .into(post_img);
                        }
                    });



        }








    }


    //-----------------------------------------------------view holder class--------------------------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     /*   if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

*/
//

        user_auth = FirebaseAuth.getInstance();
        currentUser = user_auth.getCurrentUser();
        //user_uid = user_auth.getInstance().getCurrentUser().getUid();
        user_uid = currentUser.getUid();

        fire_db_outgoing_quote_db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://barterworld-ad75e.firebaseio.com/");


        mOutgoinfQuoteDb =fire_db_outgoing_quote_db.child("Outgoing_Quotes").child(user_uid);
        mOutgoinfQuoteDb.keepSynced(true);
       // adapter_setView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }
/*

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
