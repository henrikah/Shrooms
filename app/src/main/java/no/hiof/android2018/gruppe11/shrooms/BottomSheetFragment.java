package no.hiof.android2018.gruppe11.shrooms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import no.hiof.android2018.gruppe11.shrooms.enumerator.BottomSheetItemType;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_ITEMS = "items";
    private static final String ARG_TYPE = "type";
    private BottomSheetItemType type;
    private Listener mListener;

    public static BottomSheetFragment newInstance(ArrayList<String> arrayList, BottomSheetItemType type) {
        final BottomSheetFragment fragment = new BottomSheetFragment();
        final Bundle args = new Bundle();
        int itemCount = arrayList.size();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        args.putSerializable(ARG_TYPE, type);
        args.putStringArrayList(ARG_ITEMS, arrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        type = (BottomSheetItemType)getArguments().getSerializable(ARG_TYPE);
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT), getArguments().getStringArrayList(ARG_ITEMS)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(int position, BottomSheetItemType type);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_bottom_sheet_dialog_item, parent, false));
            text = itemView.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClicked(getAdapterPosition(), type);
                        dismiss();
                    }
                }
            });
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<String> array;
        private final int mItemCount;

        ItemAdapter(int itemCount, ArrayList<String> arrayList) {
            array = arrayList;
            mItemCount = itemCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(array.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}
