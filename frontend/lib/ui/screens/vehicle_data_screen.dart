import 'package:flutter/material.dart';
import 'package:frontend/app/helpers/plate_formater.dart';
import 'package:frontend/app/models/crime.dart';
import 'package:frontend/app/models/person.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/app/services/vehicle_service.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/_core/app_background_gradient.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/widgets/crimes_list.dart';
import 'package:frontend/ui/components/widgets/dialogs/help_dialog.dart';
import 'package:frontend/ui/components/widgets/vehicle_info.dart';
import 'package:get_it/get_it.dart';
import 'package:go_router/go_router.dart';

class VehicleDataScreen extends StatelessWidget {
  final Vehicle _vehicle;
  const VehicleDataScreen(this._vehicle, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            context.go('/home');
          },
        ),
        actions: [
          IconButton(
            onPressed: () {
              showHelpDialog(context);
            },
            icon: Icon(Icons.help_outline_rounded),
          ),
        ],
        backgroundColor: AppColors.amber,
      ),
      resizeToAvoidBottomInset: false,
      body: AppBackgroundGradient(
        padding: const EdgeInsets.all(16),
        child: Ink(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisSize: MainAxisSize.max,
            children: [
              Text(
                PlateFormater.formatPlate(
                  _vehicle.plate,
                  dotMode: true,
                  withSpacing: true,
                ),
                style: TextStyle(
                  fontFamily: "GL Nummernschild",
                  fontSize: 55,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Divider(thickness: 2, height: 2, color: Colors.black),
              const SizedBox(height: 8),
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      _buildCarInfo(),
                      const SizedBox(height: 16),
                      _buildOwnerInfo(),
                    ],
                  ),
                ),
              ),
              const SizedBox(height: 8),
              Divider(height: 2, color: Colors.black),
              const SizedBox(height: 16),
              PrimaryButton(
                icon: Icons.picture_as_pdf,
                text: "Abrir PDF",
                bgColor: AppColors.lightAmber,
                onTap: () {
                  GetIt.instance<VehicleService>().downloadVehiclePDF(
                    _vehicle.plate,
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCarInfo() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          "  VEÍCULO: ",
          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
        ),
        Container(
          margin: EdgeInsets.all(8),
          padding: EdgeInsets.all(8),
          decoration: BoxDecoration(
            color: Colors.grey.shade400,
            borderRadius: BorderRadius.circular(8),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildInfoItem('Tipo: ', _vehicle.vehicleType),
              Row(
                spacing: 16,
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  _buildInfoItem('Cor: ', _vehicle.color),
                  _buildInfoItem(
                    'IPVA: ',
                    _vehicle.ipvaStatus,
                    infoStyle:
                        _vehicle.ipvaStatus == 'Atrasado' ||
                                _vehicle.ipvaStatus == 'Não Pago'
                            ? TextStyle(color: Colors.red)
                            : null,
                  ),
                ],
              ),
              _buildInfoItem(
                'Modelo: ',
                "${_vehicle.brand} ${_vehicle.model} - ${_vehicle.fabricationYear}",
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildOwnerInfo() {
    Person owner = _vehicle.owner;
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          "  PROPRIETÁRIO: ",
          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
        ),
        Container(
          margin: EdgeInsets.all(8),
          padding: EdgeInsets.all(8),
          clipBehavior: Clip.hardEdge,
          decoration: BoxDecoration(
            color: Colors.grey.shade400,
            borderRadius: BorderRadius.circular(8),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildInfoItem('Nome: ', owner.fullName),
              _buildInfoItem('Data de Nasc.: ', owner.birthDate),
              Row(
                spacing: 16,
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  _buildInfoItem('RG: ', owner.rg),
                  _buildInfoItem('Cat. CNH: ', owner.licenseCategory),
                ],
              ),
              _buildInfoItem("CPF: ", owner.cpf),
              _buildInfoItem(
                'Endereço: ',
                '${owner.address.street}, ${owner.address.number} - ${owner.address.neighborhood}',
              ),
              SizedBox(height: 16),
              Text(
                "FICHA CRIMINAL: ",
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  children:
                      owner.crimesList.isNotEmpty
                          ? List.generate(owner.crimesList.length, (index) {
                            return _buildCrimeCard(owner.crimesList[index]);
                          })
                          : [
                            Padding(
                              padding: EdgeInsets.only(top: 8),
                              child: Text(
                                'Ficha criminal limpa...',
                                style: TextStyle(
                                  fontSize: 16,
                                  color: Colors.grey.shade600,
                                ),
                              ),
                            ),
                          ],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildCrimeCard(Crime crime) {
    return Card(
      color:
          crime.crimeStatus.contains('ena') || crime.crimeStatus == 'Condenado'
              ? Colors.red.shade100
              : Colors.yellow.shade200,
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: SizedBox(
          width: 220,
          height: 128,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                crime.crimeType,
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
              _buildInfoItem('Status: ', crime.crimeStatus),
              _buildInfoItem('Data: ', crime.crimeDateTime),
              _buildInfoItem('Resumo:', ''),
              _buildInfoItem('', crime.description, maxLines: 2),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildInfoItem(
    String title,
    String info, {
    int maxLines = 1,
    TextStyle? infoStyle,
  }) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(title, style: TextStyle(fontWeight: FontWeight.bold)),
        Flexible(
          child: Text(
            info,
            style: infoStyle,
            maxLines: maxLines,
            overflow: TextOverflow.ellipsis,
            softWrap: false,
          ),
        ),
      ],
    );
  }
}
