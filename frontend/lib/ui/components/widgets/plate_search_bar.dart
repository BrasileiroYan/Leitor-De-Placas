import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';

class PlateSearchBar extends StatefulWidget {
  final PlateSearchViewModel viewModel;
  const PlateSearchBar(this.viewModel, {super.key});

  @override
  State<PlateSearchBar> createState() => _PlateSearchBarState();
}

class _PlateSearchBarState extends State<PlateSearchBar> {
  final FocusNode _focusNode = FocusNode();

  @override
  void initState() {
    widget.viewModel.searchController.text = '';
    super.initState();
  }

  @override
  void dispose() {
    _focusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<String>?>(
      future: widget.viewModel.fetchSearchScope(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return RawAutocomplete<String>(
            textEditingController: widget.viewModel.searchController,
            focusNode: _focusNode,
            optionsBuilder: (TextEditingValue textEditingValue) {
              final query = textEditingValue.text.trim().toUpperCase();

              if (query.isEmpty) {
                return widget.viewModel.searchScope!;
              }

              return widget.viewModel.searchScope!.where(
                (plate) => plate.startsWith(query),
              );
            },
            onSelected: (String selection) async {
              widget.viewModel.searchController.text = selection;
              await _onPlateSubmitted();
            },
            fieldViewBuilder: (
              context,
              textEditingController,
              focusNode,
              onFieldSubmitted,
            ) {
              return TextField(
                controller: widget.viewModel.searchController,
                textCapitalization: TextCapitalization.characters,
                focusNode: focusNode,
                autofocus: true,
                decoration: const InputDecoration(
                  hintText: 'Pesquisar placa...',
                  hintStyle: TextStyle(fontFamily: '', fontSize: 14),
                ),
                style: const TextStyle(
                  color: Colors.white,
                  fontFamily: "GL Nummernschild",
                  fontWeight: FontWeight.bold,
                  fontSize: 20,
                ),
                onChanged: (value) {
                  // Force uppercase and preserve cursor position
                  widget
                      .viewModel
                      .searchController
                      .value = widget.viewModel.searchController.value.copyWith(
                    text: value.toUpperCase(),
                    selection: TextSelection.collapsed(offset: value.length),
                  );
                },
                onSubmitted: (value) async {
                  await _onPlateSubmitted();
                },
              );
            },
            optionsViewBuilder: (context, onSelected, options) {
              return Padding(
                padding: EdgeInsets.zero,
                child: Align(
                  alignment: AlignmentDirectional.topStart,
                  child: Material(
                    elevation: 4.0,
                    child: ConstrainedBox(
                      constraints: BoxConstraints(
                        maxHeight: 200,
                        maxWidth: 200,
                      ),
                      child: ListView.builder(
                        padding: EdgeInsets.symmetric(horizontal: 2),
                        shrinkWrap: true,
                        itemCount: options.length,
                        itemBuilder: (BuildContext context, int index) {
                          final String option = options.elementAt(index);
                          return InkWell(
                            onTap: () {
                              onSelected(option);
                            },
                            child: Builder(
                              builder: (BuildContext context) {
                                final bool highlight =
                                    AutocompleteHighlightedOption.of(context) ==
                                    index;
                                if (highlight) {
                                  SchedulerBinding.instance
                                      .addPostFrameCallback(
                                        (Duration timeStamp) {
                                          Scrollable.ensureVisible(
                                            context,
                                            alignment: 0.5,
                                          );
                                        },
                                        debugLabel:
                                            'AutocompleteOptions.ensureVisible',
                                      );
                                }
                                return Container(
                                  color:
                                      highlight
                                          ? Theme.of(context).focusColor
                                          : null,
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: 16,
                                    vertical: 8,
                                  ),
                                  child: Text(
                                    RawAutocomplete.defaultStringForOption(
                                      option,
                                    ),
                                    style: TextStyle(
                                      fontFamily: "GL Nummernschild",
                                      fontWeight: FontWeight.bold,
                                      fontSize: 20,
                                    ),
                                  ),
                                );
                              },
                            ),
                          );
                        },
                      ),
                    ),
                  ),
                ),
              );
            },
          );
        } else {
          return SizedBox.shrink();
        }
      },
    );
  }

  _onPlateSubmitted() async {
    await widget.viewModel.searchPlateFromText(context);
    widget.viewModel.setSearching(!widget.viewModel.isSearching);
    if (!widget.viewModel.isSearching) {
      widget.viewModel.searchController.text = ' ';
      _focusNode.unfocus();
    }
  }
}
