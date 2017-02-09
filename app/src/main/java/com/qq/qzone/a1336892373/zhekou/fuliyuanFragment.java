package com.qq.qzone.a1336892373.zhekou;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class fuliyuanFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentPagerAdapter adapter;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();//页卡视图集合

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuliyuan, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);

        //添加页卡视图
        for (int i=1;   i<=10;   i++){
            viewpager_item_fragment fragment = new viewpager_item_fragment();
            if (i == 1)     fragment.Num = 514200;
            else            fragment.Num = i-1;   //传递类型编号
            mFragmentList.add(fragment);
        }

        //添加页卡标题
        mTitleList.add("全部");
        mTitleList.add("女装");
        mTitleList.add("男装");
        mTitleList.add("鞋包");
        mTitleList.add("配饰");
        mTitleList.add("美食");
        mTitleList.add("数码");
        mTitleList.add("美妆");
        mTitleList.add("文体");
        mTitleList.add("预告");

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为左右滚动
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(4)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(5)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(6)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(7)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(8)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(9)));

        adapter = new FragAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器

        return view;
    }

    public class FragAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        public FragAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int postion) {
            return list.get(postion);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {   }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }



}