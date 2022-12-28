package com.serieschecker.SeriesChecker.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.serieschecker.SeriesChecker.models.TitleModel;
import com.serieschecker.SeriesChecker.service.impl.TitleServiceImpl;
import com.serieschecker.SeriesChecker.view.TitleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/title")
public class TitleApi {
    private final TitleServiceImpl titleService;

    @Autowired
    public TitleApi(TitleServiceImpl titleService) {
        this.titleService = titleService;
    }

    @GetMapping
    @JsonView(TitleView.IdAndName.class)
    public List<TitleModel> listAll() {
        return titleService.titleRepository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(TitleView.FullData.class)
    public TitleModel findById(@PathVariable(value = "id") TitleModel titleModel) {
        return titleModel;
    }

    @PostMapping
    public TitleModel addTitleModel(@RequestBody TitleModel titleModel) {
        titleService.save(titleModel);
        return titleModel;
    }

    @PutMapping("{id}")
    public TitleModel updateTitleModel(@PathVariable(value = "id") TitleModel titleFromDB,
                                       @RequestBody TitleModel titleModel) {
        if (titleFromDB == null) return null;

        titleFromDB.setTitleSeasonNumber(titleModel.getTitleSeasonNumber());
        titleService.save(titleFromDB);

        return titleFromDB;
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable(value = "id") Long titleId) {
        titleService.delete(titleId);
    }
}
