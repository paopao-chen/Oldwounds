package com.example.oldwounds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oldwounds.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Create by Politness Chen on 2019/10/16--14:04
 * desc:   历史Fragment
 */
public class OldFragment extends Fragment {
    private static OldFragment oldFragment;
    private OldFragment(){}

    public static Fragment getInstance(){
        if (oldFragment == null)
            oldFragment = new OldFragment();
        return oldFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_old,container,false);
        return view;
    }
}
