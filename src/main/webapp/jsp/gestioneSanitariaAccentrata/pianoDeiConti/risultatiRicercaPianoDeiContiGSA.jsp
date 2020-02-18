<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>


<div id = "alberoDeiConti" class="modal-body">
</div>
				
<h4><span id="id_num_result" class="num_result"></span></h4>
<table id="tabellaPianoDeiConti" class="table table-hover tab_left" style="min-height:160px">   
	
	<thead>
		<tr>
			<th>Classe</th>
			<th>Livello</th>
			<th>Conto</th>
			<th>Stato</th>
			<th>Conto di legge</th>
			<th>Conto foglia</th>
			<th class="tab_Right span1">&nbsp;</th>
		</tr>
		
		<tr class="tabRowLight" id = "rigaDatiPadre">
		</tr>
	</thead>
	
	<tbody>
	</tbody>
	
</table>

<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
	<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<p><strong>Attenzione!</strong></p>
			<p>
				Stai per annullare l'elemento selezionato: sei sicuro di voler proseguire?
			</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<button class="btn btn-primary" formmethod="post" type="submit" formaction="ricercaPianoDeiContiGSA_annulla.do">si, prosegui</button>
	</div>
</div>

<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	<s:hidden id="HIDDEN_UidDaEliminare" name="uidDaEliminare" />
	<div class="modal-body">
		<div class="alert alert-error">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<p><strong>Attenzione!</strong></p>
			<p>
				Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?
			</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<button class="btn btn-primary" formmethod="post" type="submit" formaction="ricercaPianoDeiContiGSA_elimina.do">si, prosegui</button>
	</div>
</div>