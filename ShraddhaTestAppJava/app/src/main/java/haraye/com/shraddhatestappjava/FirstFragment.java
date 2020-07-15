package haraye.com.shraddhatestappjava;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import haraye.com.shraddhatestappjava.model.DataModel;

public class FirstFragment extends Fragment {

    private MainViewModel viewModel;
    DataListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new DataListAdapter(getActivity());

        adapter.setOnItemClickListner(new DataListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(DataModel data) {
                Intent intent = new Intent();
                intent.putExtra("DATA_ID", data.dataid);
                intent.setClass(getActivity(), CreateDataActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(DataModel data) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        getActivity());

                // Setting Dialog Title
                dialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                dialog.setMessage("Are you sure you want delete this data?");

                // Setting Icon to Dialog
                dialog.setIcon(R.drawable.ic_delete_forever_black_24dp);

                // Setting Positive "Yes" Btn
                dialog.setPositiveButton("YES",
                        (dialog12, which) -> {


                            viewModel.delete(data);
                            // Write your code here to execute after dialog
                            Toast.makeText(getActivity(),
                                    "Deleted!", Toast.LENGTH_SHORT)
                                    .show();


                            refreshData();

                        });
                // Setting Negative "NO" Btn
                dialog.setNegativeButton("NO",
                        (dialog1, which) -> dialog1.cancel());

// Showing Alert Dialog
                dialog.show();
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void onResume() {
        super.onResume();

        refreshData();

    }

    private void refreshData() {
        AsyncTask.execute(() -> {
            List<DataModel> data = viewModel.getAllData();
            getActivity().runOnUiThread(() -> adapter.setData(data));

        });
    }
}
